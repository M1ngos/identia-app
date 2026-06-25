package com.identia.app.ml

import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertTrue

class IdCardDetectorTest {

    // Gradle runs jvmTest with the module dir as the working directory.
    private val modelFile = File("src/commonMain/composeResources/files/best_recent.onnx")

    private fun modelBytes(): ByteArray {
        assertTrue(modelFile.exists(), "model not found at ${modelFile.absolutePath}")
        return modelFile.readBytes()
    }

    @Test
    fun modelRunsAndHasYoloOutputShape() = runBlocking {
        createOnnxSession(modelBytes()).use { session ->
            val zeros = OnnxValue(FloatArray(3 * 640 * 640), longArrayOf(1, 3, 640, 640))
            val outputs = session.run(mapOf(IdCardDetector.INPUT_NAME to zeros))
            val out = outputs.getValue(IdCardDetector.OUTPUT_NAME)
            // YOLO11 detect: [1, 4 + 14 classes, 8400 anchors]
            assertContentEquals(longArrayOf(1, 18, 8400), out.shape)
        }
    }

    @Test
    fun detectReturnsValidBoxesForSyntheticImage() = runBlocking {
        val detector = IdCardDetector(createOnnxSession(modelBytes()))
        try {
            val w = 320
            val h = 200
            val pixels = IntArray(w * h) { 0xFF808080.toInt() } // mid-gray
            val detections = detector.detect(pixels, w, h)
            // Synthetic input may yield zero detections; any returned must be valid.
            for (d in detections) {
                assertTrue(d.classId in ID_CARD_CLASSES.indices, "bad classId ${d.classId}")
                assertTrue(d.score >= detector.config.confThreshold, "score below threshold: ${d.score}")
                assertTrue(d.box.x1 in 0f..w.toFloat() && d.box.x2 in 0f..w.toFloat(), "x out of bounds: ${d.box}")
                assertTrue(d.box.y1 in 0f..h.toFloat() && d.box.y2 in 0f..h.toFloat(), "y out of bounds: ${d.box}")
            }
        } finally {
            detector.close()
        }
    }
}
