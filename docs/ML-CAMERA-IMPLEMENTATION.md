# ML & Camera — Implementation State

Status of the on-device inference and camera capture work in the `:shared` Kotlin
Multiplatform module. Scope: ONNX Runtime inference, live camera capture, and the
YOLO-based ID-card detector wired into the verification flow.

**Last updated:** 2026-06-25

## Targets at a glance

| Capability | Android | JVM (Desktop) | iOS |
|---|---|---|---|
| ONNX inference engine | ✅ real (`onnxruntime-android`) | ✅ real (`onnxruntime`) | ⛔ stub (throws) |
| Camera preview + capture | ✅ CameraX (via CameraK) | ✅ JavaCV (via CameraK) | ✅ AVFoundation (via CameraK) |
| Image decode (bytes → ARGB) | ✅ `BitmapFactory` | ✅ `ImageIO` | ⛔ stub (null) |
| ID detection on capture | ✅ end-to-end | ✅ end-to-end | ⛔ skipped (engine + decode stubbed) |
| Field OCR (text from boxes) | ✅ ML Kit (Latin) | ⛔ stub (null) | ⛔ stub (pending Vision) |
| No-camera detection | n/a (assumed present) | ✅ scans `/dev/video*` | n/a (assumed present) |

"⛔ stub" means the code compiles and runs but returns null / throws, and the
calling flow degrades gracefully (capture still advances).

---

## 1. ONNX inference engine (`com.identia.app.ml`)

Platform-agnostic API in `commonMain`; the only `expect` is the session factory.

- `OnnxValue(data: FloatArray, shape: LongArray)` — tensor wrapper (no `ai.onnxruntime` types leak into common)
- `OnnxSession` — `suspend fun run(inputs): outputs` + `close()`; `use {}` helper
- `OnnxOptions` — `intraOpThreads`
- `expect fun createOnnxSession(modelBytes, options): OnnxSession`
- `loadModelBytes(path)` — loads a model from Compose Resources (`composeResources/files/…`), identical on all targets
- `FaceMath.kt` — `cosineSimilarity` + `FaceEmbedder` (future face-match hook; not yet used)

Actuals:
- **Android** (`OnnxEngine.android.kt`) + **JVM** (`OnnxEngine.jvm.kt`) — byte-for-byte
  identical, backed by the `ai.onnxruntime` Java API. They are **not** in a shared
  intermediate source set: a manual `dependsOn` disables the default KMP hierarchy
  template and breaks iOS expect/actual linkage, so the two files are kept in sync
  by hand (noted in their headers).
- **iOS** (`OnnxEngine.ios.kt`) — stub throwing `NotImplementedError`. Next step:
  CocoaPods `onnxruntime-c` pod + cinterop.

## 2. ID-card detector (`com.identia.app.ml`)

Model: **Ultralytics YOLO11n**, `composeResources/files/best_recent.onnx` (~10 MB).
- Input `images`: `[1, 3, 640, 640]` float, RGB, NCHW, normalized 0–1, letterboxed
- Output `output0`: `[1, 18, 8400]` = 4 bbox + 14 class scores, **no built-in NMS**
- Classes (channel order): `front_card, back_card, name, id_number, dob, pob,
  father_name, mother_name, valid, poi, sex, address, doi, photo`

Pipeline:
- `ImagePreprocessing.letterbox(pixels, w, h, size=640)` → `OnnxValue` + `LetterboxInfo`
  (nearest-neighbour, 114 padding — dependency-free, cross-platform)
- `IdCardDetector.load()` → `detect(pixels, w, h): List<Detection>`
  - decodes `[1,18,8400]`, confidence-filters (default 0.25), maps boxes back to
    original image coords, then per-class NMS (default IoU 0.45)
- `Detection(classId, label, score, box: BoundingBox)`; `ID_CARD_CLASSES` label list

Config via `IdDetectorConfig(inputSize, confThreshold, iouThreshold, classNames)`.

## 3. Image decoding (`com.identia.app.ml`)

- `DecodedImage(pixels, width, height)` — packed ARGB, row-major
- `expect suspend fun decodeImageToArgb(bytes): DecodedImage?`
- `expect suspend fun readFileBytes(path): ByteArray?`

Actuals: Android (`BitmapFactory`), JVM (`ImageIO`), iOS (stub → null).

## 4. Camera (CameraK + capture flow)

Library: **CameraK** `io.github.kashif-mehmood-km:camerak:0.4` (the maintained
"Kamera"), added to `commonMain`. Android = CameraX, iOS = AVFoundation, Desktop = JavaCV.

Permissions:
- Android: `CAMERA` permission + `camera.any` feature (`required=false`) in `androidApp/.../AndroidManifest.xml`
- iOS: `NSCameraUsageDescription` + `NSPhotoLibraryAddUsageDescription` in `iosApp/iosApp/Info.plist`

`CaptureIdScreen` behaviour:
1. `isCameraAvailable()` pre-check (`com.identia.app.camera`). On Linux desktop it
   scans `/dev/video*`; if none, the screen shows **"No camera detected"** and never
   starts CameraK (which would otherwise only log a grabber failure and leave a
   blank preview — it does not emit an `Error` state on desktop).
2. When available: `rememberCameraKState` (back lens, JPEG, 4:3) renders a live
   `CameraPreviewView` behind the existing viewfinder overlay; states map to
   spinner / preview / error message.
3. **Shutter** → `takePictureToFile()` → bytes (`Success.byteArray`, or read
   `SuccessWithFile.filePath`) → `decodeImageToArgb` → `IdCardDetector.detect` →
   `extractFields` (OCR, see §5). **Soft gating:**
   - expected side (`front_card`/`back_card`) found → run OCR, record fields, advance
   - ran but not found → stay, show "Couldn't detect the ID…" hint (retry)
   - detector/decoder unavailable (iOS stub, decode failure) → advance (never blocks)

   OCR never gates progression: a failure or unavailable backend just yields no
   fields. Results are handed up via `onExtracted` and merged into `DemoState`
   (`recordIdFields`, latest read of a field wins across the front/back captures).

## 5. Field OCR (`com.identia.app.ml`)

The detector *locates* field boxes; OCR *reads the text* inside them. Recognition
is platform-native — the on-device engine beats a small bundled model on accuracy
and adds no weight to the binary. The ONNX YOLO detector remains the only model
shipped; OCR is a separate, swappable backend behind an `expect`/`actual` seam
(same shape as `decodeImageToArgb` / `createOnnxSession`).

- `recognizeText(image): String?` — `expect`; OCR a single cropped field.
- `isTextRecognitionAvailable: Boolean` — `expect`; per-target capability flag.
- `DecodedImage.crop(box, padFraction=0.06)` — pure-Kotlin ARGB crop in source
  pixel coords (the same space as `Detection.box`), clamped to bounds, with a small
  margin (recognizers do better with a few px of quiet space).
- `extractFields(image, detections, …): List<ExtractedField>` — crops every
  text-bearing detection (skips `NON_TEXT_LABELS` = `front_card`/`back_card`/`photo`),
  OCRs it highest-confidence first, drops blanks. `available` + `recognize` are
  injectable for tests. Returns empty (never throws) when OCR is unavailable.
- `ExtractedField(label, text, detectionScore, box)` — one read field.

Actuals:
- **Android** (`TextRecognition.android.kt`) — **real**: ML Kit Text Recognition
  (`com.google.mlkit:text-recognition`, bundled Latin model, on-device). ARGB →
  `Bitmap` → `InputImage`; `Task` bridged to `suspend` via `suspendCancellableCoroutine`.
- **JVM** (`TextRecognition.jvm.kt`) — stub (`available=false`, returns null). No
  built-in OCR; desktop isn't a capture target. Wire tess4j if ever needed.
- **iOS** (`TextRecognition.ios.kt`) — stub (`available=false`). Recognition needs
  only pixels (no ONNX), but iOS `decodeImageToArgb` is null today so no crop ever
  reaches it. TODO in-file: implement via Vision (`VNRecognizeTextRequest`, no extra
  dependency) when iOS decode lands.

## Tests (`:shared`)

- `OnnxEngineTest` (jvmTest) — real inference on a tiny embedded Add model
- `IdCardDetectorTest` (jvmTest) — loads `best_recent.onnx`, asserts `[1,18,8400]`
  output, validates `detect()` output bounds
- `IdCardDetectorLogicTest` (commonTest) — letterbox, IoU, NMS
- `TextRecognitionLogicTest` (commonTest) — `crop` bounds/clamping, `extractFields`
  ordering, non-text/blank skipping, unavailable short-circuit (fake recognizer)
- `FaceMathTest` (commonTest) — cosine similarity

Run: `./gradlew :shared:jvmTest`. Compile checks:
`./gradlew :shared:compileKotlinJvm :shared:compileKotlinIosSimulatorArm64 :shared:compileAndroidMain`

## Known gaps / next steps

- **iOS inference + OCR**: implement the ONNX session (CocoaPods `onnxruntime-c` +
  cinterop), the CoreGraphics image decode, and Vision-based `recognizeText`; until
  decode lands, iOS capture skips both detection and OCR.
- **Android EXIF rotation**: captured photos aren't rotated to upright before
  `getPixels`; rotated input lowers detection *and* OCR accuracy. Read EXIF first.
- **Surfacing OCR fields**: `DemoState.idFields` now holds the read values, but no
  screen renders/edits them yet (e.g. a review-and-confirm step before submit).
- **OCR field post-processing**: raw recognizer text isn't validated/normalized
  per field (e.g. digits-only for `id_number`, date parsing for `dob`).
- **Face flow**: `FaceMatchScreen` still uses mock confidence; `FaceEmbedder` is a
  hook only — no face model wired.
- **Bundle size**: the 10 MB model ships in every target's resources.
