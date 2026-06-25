package com.identia.app.ml

import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

actual suspend fun decodeImageToArgb(bytes: ByteArray): DecodedImage? = withContext(Dispatchers.Default) {
    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: return@withContext null
    val width = bitmap.width
    val height = bitmap.height
    val pixels = IntArray(width * height)
    bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
    bitmap.recycle()
    DecodedImage(pixels, width, height)
}

actual suspend fun readFileBytes(path: String): ByteArray? = withContext(Dispatchers.IO) {
    runCatching { File(path).readBytes() }.getOrNull()
}
