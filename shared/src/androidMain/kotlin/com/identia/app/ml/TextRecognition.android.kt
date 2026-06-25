package com.identia.app.ml

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

actual val isTextRecognitionAvailable: Boolean = true

// The recognizer is thread-safe and reusable; build it once. It owns native
// resources but lives for the process — there's no detector-like lifecycle here.
private val recognizer by lazy {
    TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
}

actual suspend fun recognizeText(image: DecodedImage): String? {
    // ML Kit reads from a Bitmap; our pixels are already packed ARGB.
    val bitmap = Bitmap.createBitmap(
        image.pixels, image.width, image.height, Bitmap.Config.ARGB_8888,
    )
    val input = InputImage.fromBitmap(bitmap, /* rotationDegrees = */ 0)
    return suspendCancellableCoroutine { cont ->
        recognizer.process(input)
            .addOnSuccessListener { result ->
                bitmap.recycle()
                cont.resume(result.text.ifBlank { null })
            }
            .addOnFailureListener {
                bitmap.recycle()
                cont.resume(null)
            }
    }
}
