package com.identia.app.ml

/**
 * Create an [OnnxSession] from a serialized ONNX model.
 *
 * This is the single `expect` seam of the inference layer:
 * - Android + JVM desktop share one actual backed by the `ai.onnxruntime` Java API.
 * - iOS currently provides a stub (binding pending: CocoaPods `onnxruntime-c` + cinterop).
 *
 * @param modelBytes the raw `.onnx` model file contents (see [loadModelBytes]).
 * @param options backend-neutral session tuning.
 */
expect fun createOnnxSession(
    modelBytes: ByteArray,
    options: OnnxOptions = OnnxOptions(),
): OnnxSession
