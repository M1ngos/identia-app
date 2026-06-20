package com.identia.app.ml

/**
 * Backend-neutral session tuning knobs. Each platform maps these onto its own
 * ONNX Runtime session options where supported.
 *
 * @param intraOpThreads threads used to parallelize a single operator. Default 1
 *   keeps inference deterministic and light on mobile.
 */
data class OnnxOptions(
    val intraOpThreads: Int = 1,
)
