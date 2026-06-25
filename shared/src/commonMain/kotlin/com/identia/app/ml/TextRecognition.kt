package com.identia.app.ml

/**
 * On-device OCR: reads the text inside an already-cropped field image.
 *
 * Recognition is platform-native — the engine that ships with the device beats a
 * small bundled model on accuracy and adds no weight to the binary:
 *  - **Android**: ML Kit Text Recognition (Latin script, on-device).
 *  - **iOS**: Vision `VNRecognizeTextRequest` (pending, see actual).
 *  - **JVM/desktop**: stub (no built-in OCR; desktop isn't a target user platform).
 *
 * The ID *fields* are still located by the ONNX YOLO detector ([IdCardDetector]);
 * this only recognizes the characters within each detected box. The interface is
 * deliberately the same shape as [decodeImageToArgb] / [createOnnxSession] so an
 * ONNX recognition model could replace the native backends later without touching
 * the capture flow.
 *
 * @return recognized text (may be multi-line, trimmed), or null where OCR is
 *   unavailable or finds nothing. Never throws for an unreadable crop.
 */
expect suspend fun recognizeText(image: DecodedImage): String?

/** Whether on-device OCR is implemented on the current target. */
expect val isTextRecognitionAvailable: Boolean

/** A detected field whose text has been read off the card. */
data class ExtractedField(
    val label: String,
    val text: String,
    val detectionScore: Float,
    val box: BoundingBox,
)

/**
 * Field detection labels that carry no readable text and so are skipped by OCR:
 * the whole-card boxes and the photo.
 */
val NON_TEXT_LABELS: Set<String> = setOf("front_card", "back_card", "photo")

/**
 * Crop every text-bearing detection out of [image] and OCR it, returning the
 * fields whose text was read.
 *
 * Detections are processed highest-confidence first; whole-card and photo boxes
 * (see [NON_TEXT_LABELS]) are skipped. Fields that recognize to blank — or any
 * field at all when OCR is unavailable — are dropped, so an empty result simply
 * means "nothing read" and the caller can proceed without gating on it.
 *
 * @param padFraction margin added around each box before OCR (see [DecodedImage.crop]).
 * @param available short-circuits to an empty result when false; defaults to the
 *   platform capability. Overridable for tests.
 * @param recognize the OCR backend; defaults to the platform [recognizeText].
 *   Overridable for tests.
 */
suspend fun extractFields(
    image: DecodedImage,
    detections: List<Detection>,
    padFraction: Float = 0.06f,
    available: Boolean = isTextRecognitionAvailable,
    recognize: suspend (DecodedImage) -> String? = ::recognizeText,
): List<ExtractedField> {
    if (!available) return emptyList()
    val out = ArrayList<ExtractedField>()
    for (det in detections.sortedByDescending { it.score }) {
        if (det.label in NON_TEXT_LABELS) continue
        val crop = image.crop(det.box, padFraction) ?: continue
        val text = recognize(crop)?.trim()
        if (!text.isNullOrEmpty()) {
            out.add(ExtractedField(det.label, text, det.score, det.box))
        }
    }
    return out
}
