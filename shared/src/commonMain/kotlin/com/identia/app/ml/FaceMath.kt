package com.identia.app.ml

import kotlin.math.sqrt

/**
 * Integration seam for the face-verification flow. A future face model wraps an
 * [OnnxSession] behind this interface; the UI (e.g. `FaceMatchScreen`) then
 * compares embeddings with [cosineSimilarity] instead of using mock confidence.
 */
interface FaceEmbedder {
    /** Map a preprocessed face tensor to an L2-comparable embedding vector. */
    suspend fun embed(input: OnnxValue): FloatArray
}

/**
 * Cosine similarity of two embeddings, in `[-1, 1]`. Higher means more similar.
 * Returns `0` if either vector has zero magnitude.
 *
 * @throws IllegalArgumentException if the vectors differ in length.
 */
fun cosineSimilarity(a: FloatArray, b: FloatArray): Float {
    require(a.size == b.size) { "embedding length mismatch: ${a.size} vs ${b.size}" }
    var dot = 0f
    var normA = 0f
    var normB = 0f
    for (i in a.indices) {
        dot += a[i] * b[i]
        normA += a[i] * a[i]
        normB += b[i] * b[i]
    }
    val denom = sqrt(normA) * sqrt(normB)
    return if (denom == 0f) 0f else dot / denom
}
