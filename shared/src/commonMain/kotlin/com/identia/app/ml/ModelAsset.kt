package com.identia.app.ml

import identia_app.shared.generated.resources.Res

/**
 * Load an ONNX model bundled as a Compose Multiplatform resource. This works
 * identically on every target, so the call site stays in `commonMain`.
 *
 * Place `.onnx` files under `shared/src/commonMain/composeResources/files/`,
 * then pass the path relative to `composeResources`, e.g.
 * `loadModelBytes("files/face_encoder.onnx")`.
 *
 * @param path resource path relative to `composeResources` (e.g. `"files/model.onnx"`).
 * @return the raw model bytes, ready for [createOnnxSession].
 */
suspend fun loadModelBytes(path: String): ByteArray = Res.readBytes(path)
