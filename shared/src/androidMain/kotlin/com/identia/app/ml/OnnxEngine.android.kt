package com.identia.app.ml

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.FloatBuffer

/**
 * Android implementation, backed by the ONNX Runtime Java API (`ai.onnxruntime`,
 * via `onnxruntime-android`).
 *
 * NOTE: byte-for-byte identical to the JVM desktop actual in
 * `OnnxEngine.jvm.kt` — keep the two in sync. (No shared JVM/Android source set
 * exists here because a manual intermediate set disables the default KMP
 * hierarchy template and breaks the iOS expect/actual linkage.)
 */
actual fun createOnnxSession(modelBytes: ByteArray, options: OnnxOptions): OnnxSession {
    val env = OrtEnvironment.getEnvironment()
    val sessionOptions = OrtSession.SessionOptions().apply {
        setIntraOpNumThreads(options.intraOpThreads)
    }
    val session = env.createSession(modelBytes, sessionOptions)
    return JvmOnnxSession(env, session)
}

private class JvmOnnxSession(
    private val env: OrtEnvironment,
    private val session: OrtSession,
) : OnnxSession {

    override suspend fun run(inputs: Map<String, OnnxValue>): Map<String, OnnxValue> =
        withContext(Dispatchers.Default) {
            val tensors = inputs.mapValues { (_, value) ->
                OnnxTensor.createTensor(env, FloatBuffer.wrap(value.data), value.shape)
            }
            try {
                session.run(tensors).use { result ->
                    buildMap {
                        for (entry in result) {
                            val tensor = entry.value as? OnnxTensor ?: continue
                            val buffer = tensor.floatBuffer ?: continue
                            val data = FloatArray(buffer.remaining()).also(buffer::get)
                            put(entry.key, OnnxValue(data, tensor.info.shape))
                        }
                    }
                }
            } finally {
                tensors.values.forEach(OnnxTensor::close)
            }
        }

    override fun close() {
        session.close()
        // `env` is a shared singleton owned by ORT; do not close it here.
    }
}
