package com.identia.app.ml

/**
 * Tuning for [IdCardDetector].
 *
 * @param inputSize square model input size (YOLO11n: 640).
 * @param confThreshold minimum class score to keep a detection.
 * @param iouThreshold IoU above which overlapping same-class boxes are suppressed.
 * @param classNames label per class id, in model output order.
 */
data class IdDetectorConfig(
    val inputSize: Int = 640,
    val confThreshold: Float = 0.25f,
    val iouThreshold: Float = 0.45f,
    val classNames: List<String> = ID_CARD_CLASSES,
)

/**
 * ID-card field detector backed by the Ultralytics YOLO11n model
 * (`files/best_recent.onnx`). Detects the card and its fields (name, id_number,
 * dob, photo, …).
 *
 * Usage:
 * ```
 * val detector = IdCardDetector.load()
 * val fields = detector.detect(pixels, width, height)
 * detector.close()
 * ```
 *
 * Owns native resources via the wrapped [OnnxSession]; call [close] when done.
 */
class IdCardDetector(
    private val session: OnnxSession,
    val config: IdDetectorConfig = IdDetectorConfig(),
) {
    /**
     * Detect ID fields in an ARGB image (see [letterbox] for the pixel format).
     *
     * @return detections in the original image's pixel coordinates, sorted by
     *   descending score, after confidence filtering and per-class NMS.
     */
    suspend fun detect(pixels: IntArray, width: Int, height: Int): List<Detection> {
        val (input, info) = letterbox(pixels, width, height, config.inputSize)
        val outputs = session.run(mapOf(INPUT_NAME to input))
        val output = outputs[OUTPUT_NAME]
            ?: error("model produced no '$OUTPUT_NAME' output (got ${outputs.keys})")
        val raw = decode(output, info, width, height)
        return nonMaxSuppression(raw, config.iouThreshold)
    }

    fun close() = session.close()

    /**
     * Decode YOLO11 detect output `[1, 4 + nc, 8400]` into boxes mapped back to
     * original-image coordinates. Channels per prediction `i`: `cx, cy, w, h`
     * (model-space px) followed by `nc` already-activated class scores.
     */
    private fun decode(output: OnnxValue, info: LetterboxInfo, srcW: Int, srcH: Int): List<Detection> {
        val shape = output.shape
        require(shape.size == 3 && shape[0] == 1L) { "unexpected output shape ${shape.toList()}" }
        val channels = shape[1].toInt()
        val predictions = shape[2].toInt()
        val nc = channels - 4
        require(nc >= 1) { "output has no class channels (channels=$channels)" }

        val data = output.data
        val results = ArrayList<Detection>()
        for (i in 0 until predictions) {
            // Best class for this prediction.
            var bestClass = -1
            var bestScore = 0f
            for (c in 0 until nc) {
                val s = data[(4 + c) * predictions + i]
                if (s > bestScore) {
                    bestScore = s
                    bestClass = c
                }
            }
            if (bestClass < 0 || bestScore < config.confThreshold) continue

            val cx = data[i]
            val cy = data[predictions + i]
            val w = data[2 * predictions + i]
            val h = data[3 * predictions + i]

            // model space -> original image
            val x1 = ((cx - w / 2f) - info.padX) / info.scale
            val y1 = ((cy - h / 2f) - info.padY) / info.scale
            val x2 = ((cx + w / 2f) - info.padX) / info.scale
            val y2 = ((cy + h / 2f) - info.padY) / info.scale

            val box = BoundingBox(
                x1 = x1.coerceIn(0f, srcW.toFloat()),
                y1 = y1.coerceIn(0f, srcH.toFloat()),
                x2 = x2.coerceIn(0f, srcW.toFloat()),
                y2 = y2.coerceIn(0f, srcH.toFloat()),
            )
            val label = config.classNames.getOrElse(bestClass) { "class_$bestClass" }
            results.add(Detection(bestClass, label, bestScore, box))
        }
        return results
    }

    companion object {
        const val INPUT_NAME = "images"
        const val OUTPUT_NAME = "output0"
        const val MODEL_PATH = "files/best_recent.onnx"

        /** Load the bundled model and build a detector. */
        suspend fun load(config: IdDetectorConfig = IdDetectorConfig()): IdCardDetector {
            val bytes = loadModelBytes(MODEL_PATH)
            return IdCardDetector(createOnnxSession(bytes), config)
        }
    }
}

/** Intersection-over-union of two boxes. */
internal fun iou(a: BoundingBox, b: BoundingBox): Float {
    val ix1 = maxOf(a.x1, b.x1)
    val iy1 = maxOf(a.y1, b.y1)
    val ix2 = minOf(a.x2, b.x2)
    val iy2 = minOf(a.y2, b.y2)
    val iw = (ix2 - ix1).coerceAtLeast(0f)
    val ih = (iy2 - iy1).coerceAtLeast(0f)
    val inter = iw * ih
    val union = a.area + b.area - inter
    return if (union <= 0f) 0f else inter / union
}

/**
 * Greedy per-class non-max suppression. Keeps the highest-scoring box and drops
 * same-class boxes overlapping it by more than [iouThreshold].
 */
internal fun nonMaxSuppression(detections: List<Detection>, iouThreshold: Float): List<Detection> {
    val sorted = detections.sortedByDescending { it.score }
    val kept = ArrayList<Detection>(sorted.size)
    val removed = BooleanArray(sorted.size)
    for (i in sorted.indices) {
        if (removed[i]) continue
        val a = sorted[i]
        kept.add(a)
        for (j in i + 1 until sorted.size) {
            if (removed[j]) continue
            val b = sorted[j]
            if (b.classId == a.classId && iou(a.box, b.box) > iouThreshold) {
                removed[j] = true
            }
        }
    }
    return kept
}
