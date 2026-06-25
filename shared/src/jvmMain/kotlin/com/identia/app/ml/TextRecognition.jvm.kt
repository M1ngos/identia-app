package com.identia.app.ml

/**
 * Desktop stub. The JVM has no built-in OCR engine, and desktop isn't a target
 * user platform for capture, so field extraction is a no-op here (the detector
 * still runs end-to-end in tests). Wire Tesseract (e.g. tess4j) if desktop OCR
 * is ever needed.
 */
actual val isTextRecognitionAvailable: Boolean = false

actual suspend fun recognizeText(image: DecodedImage): String? = null
