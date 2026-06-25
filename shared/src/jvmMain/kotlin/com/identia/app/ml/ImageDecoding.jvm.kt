package com.identia.app.ml

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.File
import javax.imageio.ImageIO

actual suspend fun decodeImageToArgb(bytes: ByteArray): DecodedImage? = withContext(Dispatchers.Default) {
    val image = ImageIO.read(ByteArrayInputStream(bytes)) ?: return@withContext null
    val width = image.width
    val height = image.height
    val pixels = IntArray(width * height)
    image.getRGB(0, 0, width, height, pixels, 0, width)
    DecodedImage(pixels, width, height)
}

actual suspend fun readFileBytes(path: String): ByteArray? = withContext(Dispatchers.IO) {
    runCatching { File(path).readBytes() }.getOrNull()
}
