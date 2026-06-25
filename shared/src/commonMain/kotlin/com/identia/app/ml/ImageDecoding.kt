package com.identia.app.ml

/**
 * A decoded raster image: packed ARGB pixels (`pixels[y * width + x]`) ready for
 * [letterbox]. Same layout Android `Bitmap.getPixels` / Java `BufferedImage.getRGB`
 * produce.
 */
data class DecodedImage(val pixels: IntArray, val width: Int, val height: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DecodedImage) return false
        return width == other.width && height == other.height && pixels.contentEquals(other.pixels)
    }

    override fun hashCode(): Int = (width * 31 + height) * 31 + pixels.contentHashCode()
}

/**
 * Decode encoded image bytes (JPEG/PNG) into ARGB pixels.
 *
 * Real on Android (BitmapFactory) and JVM (ImageIO); iOS returns null until a
 * native decoder is wired (the iOS ONNX session is likewise stubbed, so the
 * detector can't run there yet). Returns null if decoding fails.
 */
expect suspend fun decodeImageToArgb(bytes: ByteArray): DecodedImage?

/** Read a file's bytes, or null if it can't be read. Used for capture-to-file results. */
expect suspend fun readFileBytes(path: String): ByteArray?
