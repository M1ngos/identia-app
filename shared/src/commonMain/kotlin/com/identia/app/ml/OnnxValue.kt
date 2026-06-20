package com.identia.app.ml

/**
 * Platform-agnostic, float tensor passed in and out of an [OnnxSession].
 *
 * Keeping the public surface free of any `ai.onnxruntime` types lets the iOS
 * (cinterop) and Android/JVM (Java API) implementations share the same contract.
 *
 * @param data row-major (C-contiguous) tensor elements; its size must equal the
 *   product of [shape].
 * @param shape tensor dimensions, e.g. `[1, 3, 112, 112]` for a single NCHW image.
 */
class OnnxValue(val data: FloatArray, val shape: LongArray) {

    /** Total number of elements implied by [shape]. */
    val flatSize: Long get() = shape.fold(1L) { acc, dim -> acc * dim }

    init {
        require(shape.all { it > 0 }) { "shape dims must be positive: ${shape.toList()}" }
        require(data.size.toLong() == flatSize) {
            "data size ${data.size} does not match shape ${shape.toList()} (expected $flatSize)"
        }
    }
}
