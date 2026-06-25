package com.identia.app.ml

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TextRecognitionLogicTest {

    /** A 4x4 image where pixel value encodes its (x,y) so crops are verifiable. */
    private fun grid(): DecodedImage {
        val w = 4
        val h = 4
        val px = IntArray(w * h) { i -> (i / w) * 10 + (i % w) } // y*10 + x
        return DecodedImage(px, w, h)
    }

    @Test
    fun cropExtractsExactRegionWithoutPadding() {
        val img = grid()
        // Box covering columns 1..2, rows 1..2 (exclusive of x2/y2 rounding).
        val crop = img.crop(BoundingBox(1f, 1f, 3f, 3f), padFraction = 0f)!!
        assertEquals(2, crop.width)
        assertEquals(2, crop.height)
        // Expect pixels (x,y): (1,1)=11 (2,1)=12 (1,2)=21 (2,2)=22
        assertEquals(listOf(11, 12, 21, 22), crop.pixels.toList())
    }

    @Test
    fun cropClampsToImageBounds() {
        val img = grid()
        val crop = img.crop(BoundingBox(-5f, -5f, 100f, 100f), padFraction = 0f)!!
        assertEquals(4, crop.width)
        assertEquals(4, crop.height)
    }

    @Test
    fun cropReturnsNullForEmptyBox() {
        val img = grid()
        assertNull(img.crop(BoundingBox(2f, 2f, 2f, 2f), padFraction = 0f))
    }

    @Test
    fun extractFieldsSkipsNonTextBoxes() = runTest {
        val img = grid()
        val box = BoundingBox(0f, 0f, 4f, 4f)
        val detections = listOf(
            Detection(0, "front_card", 0.99f, box), // skipped: non-text
            Detection(13, "photo", 0.95f, box),      // skipped: non-text
            Detection(2, "name", 0.90f, box),
            Detection(3, "id_number", 0.80f, box),
            Detection(4, "dob", 0.70f, box),
        )
        val fields = extractFields(
            image = img,
            detections = detections,
            available = true,
            recognize = { "x" },
        )
        // front_card, photo skipped; name, id_number, dob all return "x".
        assertEquals(listOf("name", "id_number", "dob"), fields.map { it.label })
        assertTrue(fields.all { it.text == "x" })
    }

    @Test
    fun extractFieldsDropsBlankRecognitions() = runTest {
        val img = grid()
        val box = BoundingBox(0f, 0f, 4f, 4f)
        val detections = listOf(
            Detection(2, "name", 0.9f, box),
            Detection(3, "id_number", 0.8f, box),
        )
        val fields = extractFields(
            image = img,
            detections = detections,
            available = true,
            recognize = { "   " }, // whitespace-only -> trimmed to empty -> dropped
        )
        assertTrue(fields.isEmpty())
    }

    @Test
    fun extractFieldsEmptyWhenUnavailable() = runTest {
        val img = grid()
        val fields = extractFields(
            image = img,
            detections = listOf(Detection(2, "name", 0.9f, BoundingBox(0f, 0f, 4f, 4f))),
            available = false,
            recognize = { _ -> "should not be called" },
        )
        assertTrue(fields.isEmpty())
    }

    @Test
    fun extractFieldsProcessesHighestScoreFirst() = runTest {
        val img = grid()
        val box = BoundingBox(0f, 0f, 4f, 4f)
        val detections = listOf(
            Detection(4, "dob", 0.50f, box),
            Detection(2, "name", 0.95f, box),
            Detection(3, "id_number", 0.70f, box),
        )
        val fields = extractFields(
            image = img,
            detections = detections,
            available = true,
            recognize = { _ -> "v" },
        )
        assertEquals(listOf("name", "id_number", "dob"), fields.map { it.label })
    }
}
