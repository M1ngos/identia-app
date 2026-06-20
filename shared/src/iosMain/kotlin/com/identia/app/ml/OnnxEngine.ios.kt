package com.identia.app.ml

/**
 * iOS stub. Keeps the framework compiling while the native binding is pending.
 *
 * TODO(iOS ONNX Runtime): wire real inference via CocoaPods + cinterop —
 *  1. Apply the Kotlin CocoaPods plugin in `shared/build.gradle.kts`.
 *  2. Declare the `onnxruntime-c` pod (ONNX Runtime C API).
 *  3. Add a `.def` cinterop entry exposing `onnxruntime_c_api.h`.
 *  4. Replace [StubOnnxSession] with an `OrtSession`-backed implementation that
 *     mirrors the Android/JVM behaviour in `commonJvmMain`.
 */
actual fun createOnnxSession(modelBytes: ByteArray, options: OnnxOptions): OnnxSession =
    StubOnnxSession()

private class StubOnnxSession : OnnxSession {
    override suspend fun run(inputs: Map<String, OnnxValue>): Map<String, OnnxValue> =
        throw NotImplementedError(
            "iOS ONNX Runtime binding pending: CocoaPods onnxruntime-c + cinterop",
        )

    override fun close() = Unit
}
