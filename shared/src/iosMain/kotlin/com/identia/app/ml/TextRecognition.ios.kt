package com.identia.app.ml

/**
 * iOS stub. Recognition itself only needs pixels (no ONNX), but it's gated behind
 * the same native work as the rest of the iOS pipeline — image decoding
 * (`decodeImageToArgb`) returns null today, so no crop ever reaches OCR. Both land
 * together.
 *
 * TODO(iOS OCR): implement with the Vision framework — no extra dependency:
 *  1. Build a `CGImage` from [DecodedImage] (ARGB pixels → `CGBitmapContext` with
 *     `kCGImageAlphaPremultipliedFirst | kCGBitmapByteOrder32Little`).
 *  2. Run a `VNRecognizeTextRequest` (`recognitionLevel = .accurate`) via
 *     `VNImageRequestHandler(cgImage:).performRequests(_:)`.
 *  3. Join `results.topCandidates(1).string` line by line and flip
 *     [isTextRecognitionAvailable] to true.
 */
actual val isTextRecognitionAvailable: Boolean = false

actual suspend fun recognizeText(image: DecodedImage): String? = null
