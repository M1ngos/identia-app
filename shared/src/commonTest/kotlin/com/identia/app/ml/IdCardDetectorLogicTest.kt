package com.identia.app.ml

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class IdCardDetectorLogicTest {

    @Test
    fun letterboxProducesCorrectShape() {
        val pixels = IntArray(100 * 100) { 0xFFFFFFFF.toInt() } // all white
        val (tensor, info) = letterbox(pixels, 100, 100, size = 640)
        assertEquals(listOf(1L, 3L, 640L, 640L), tensor.shape.toList())
        assertEquals(6.4f, info.scale, absoluteTolerance = 1e-4f)
        assertEquals(0f, info.padX)
        assertEquals(0f, info.padY)
    }

    @Test
    fun letterboxPadsNonSquareImages() {
        // 20x10 into 20 -> scale 1, height 10, padY 5 (top rows are padding 114/255).
        val pixels = IntArray(20 * 10) { 0xFFFFFFFF.toInt() }
        val (tensor, info) = letterbox(pixels, 20, 10, size = 20)
        assertEquals(5f, info.padY)
        assertEquals(0f, info.padX)
        // Top-left is padding...
        assertEquals(114f / 255f, tensor.data[0], absoluteTolerance = 1e-4f)
        // ...a pixel in the central image band is white.
        val plane = 20 * 20
        val centerIdx = 10 * 20 + 10 // row 10, col 10 (within image band)
        assertEquals(1f, tensor.data[centerIdx], absoluteTolerance = 1e-4f)
        assertEquals(1f, tensor.data[plane + centerIdx], absoluteTolerance = 1e-4f)
    }

    @Test
    fun iouIsOneForIdenticalBoxes() {
        val b = BoundingBox(0f, 0f, 10f, 10f)
        assertEquals(1f, iou(b, b), absoluteTolerance = 1e-6f)
    }

    @Test
    fun iouIsZeroForDisjointBoxes() {
        val a = BoundingBox(0f, 0f, 10f, 10f)
        val b = BoundingBox(20f, 20f, 30f, 30f)
        assertEquals(0f, iou(a, b))
    }

    @Test
    fun nmsSuppressesOverlappingSameClass() {
        val a = Detection(0, "front_card", 0.9f, BoundingBox(0f, 0f, 10f, 10f))
        val b = Detection(0, "front_card", 0.8f, BoundingBox(1f, 1f, 11f, 11f)) // high overlap
        val kept = nonMaxSuppression(listOf(a, b), iouThreshold = 0.45f)
        assertEquals(1, kept.size)
        assertEquals(0.9f, kept.first().score)
    }

    @Test
    fun nmsKeepsOverlappingDifferentClasses() {
        val a = Detection(0, "front_card", 0.9f, BoundingBox(0f, 0f, 10f, 10f))
        val b = Detection(2, "name", 0.8f, BoundingBox(1f, 1f, 11f, 11f))
        val kept = nonMaxSuppression(listOf(a, b), iouThreshold = 0.45f)
        assertEquals(2, kept.size)
    }
}
