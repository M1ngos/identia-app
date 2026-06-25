package com.identia.app.ml

/**
 * iOS stubs. Image decoding (CoreGraphics) is pending; it's only useful once the
 * iOS ONNX session is implemented (currently stubbed in OnnxEngine.ios.kt), so
 * detection is skipped on iOS and the capture flow proceeds without gating.
 */
actual suspend fun decodeImageToArgb(bytes: ByteArray): DecodedImage? = null

actual suspend fun readFileBytes(path: String): ByteArray? = null
