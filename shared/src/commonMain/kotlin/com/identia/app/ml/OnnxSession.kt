package com.identia.app.ml

/**
 * A loaded ONNX model ready for inference. Obtain one via [createOnnxSession].
 *
 * Sessions own native resources; always [close] them (or use [use]) when done.
 * Implementations are interface-based so the iOS stub stays trivial while the
 * Android/JVM implementation wraps an `ai.onnxruntime.OrtSession`.
 */
interface OnnxSession {

    /**
     * Run a single forward pass.
     *
     * @param inputs named input tensors, keyed by the model's input names.
     * @return named output tensors, keyed by the model's output names.
     */
    suspend fun run(inputs: Map<String, OnnxValue>): Map<String, OnnxValue>

    /** Release native resources held by this session. Idempotent. */
    fun close()
}

/** Run [block] with this session and [OnnxSession.close] it afterwards. */
inline fun <R> OnnxSession.use(block: (OnnxSession) -> R): R {
    try {
        return block(this)
    } finally {
        close()
    }
}
