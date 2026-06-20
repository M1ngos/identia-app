package com.identia.app.ml

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class FaceMathTest {

    @Test
    fun identicalVectorsScoreOne() {
        val v = floatArrayOf(1f, 2f, 3f)
        assertEquals(1f, cosineSimilarity(v, v), absoluteTolerance = 1e-6f)
    }

    @Test
    fun orthogonalVectorsScoreZero() {
        assertEquals(0f, cosineSimilarity(floatArrayOf(1f, 0f), floatArrayOf(0f, 1f)), absoluteTolerance = 1e-6f)
    }

    @Test
    fun oppositeVectorsScoreMinusOne() {
        assertEquals(-1f, cosineSimilarity(floatArrayOf(1f, 1f), floatArrayOf(-1f, -1f)), absoluteTolerance = 1e-6f)
    }

    @Test
    fun zeroVectorScoresZero() {
        assertEquals(0f, cosineSimilarity(floatArrayOf(0f, 0f), floatArrayOf(1f, 2f)))
    }

    @Test
    fun similarVectorsScoreHigh() {
        val score = cosineSimilarity(floatArrayOf(1f, 2f, 3f), floatArrayOf(1.1f, 2.1f, 2.9f))
        assertTrue(score > 0.99f, "expected high similarity, got $score")
    }

    @Test
    fun mismatchedLengthsThrow() {
        assertFailsWith<IllegalArgumentException> {
            cosineSimilarity(floatArrayOf(1f), floatArrayOf(1f, 2f))
        }
    }
}
