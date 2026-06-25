package com.identia.app.ml

import kotlin.math.roundToInt

/**
 * Maps a letterboxed model-space coordinate back to the original image.
 *
 * @param scale uniform scale applied to the source image.
 * @param padX horizontal padding (px) added on the left in model space.
 * @param padY vertical padding (px) added on the top in model space.
 */
data class LetterboxInfo(val scale: Float, val padX: Float, val padY: Float)

/**
 * Letterbox-resize an ARGB image into a square float tensor for YOLO-style models.
 *
 * Produces an [OnnxValue] of shape `[1, 3, size, size]` in NCHW, RGB order,
 * normalized to `[0, 1]`, with aspect ratio preserved and the remainder filled
 * with [padValue] (Ultralytics uses 114). Resampling is nearest-neighbour to
 * stay dependency-free across platforms.
 *
 * @param pixels packed ARGB pixels, row-major (`pixels[y * srcWidth + x]`), e.g.
 *   from Android `Bitmap.getPixels` or Compose `ImageBitmap.readPixels`.
 * @return the input tensor plus the [LetterboxInfo] needed to map detections back.
 */
fun letterbox(
    pixels: IntArray,
    srcWidth: Int,
    srcHeight: Int,
    size: Int = 640,
    padValue: Int = 114,
): Pair<OnnxValue, LetterboxInfo> {
    require(srcWidth > 0 && srcHeight > 0) { "invalid source size ${srcWidth}x$srcHeight" }
    require(pixels.size >= srcWidth * srcHeight) { "pixels too small for ${srcWidth}x$srcHeight" }

    val scale = minOf(size.toFloat() / srcWidth, size.toFloat() / srcHeight)
    val newW = (srcWidth * scale).roundToInt()
    val newH = (srcHeight * scale).roundToInt()
    val padX = (size - newW) / 2f
    val padY = (size - newH) / 2f

    val plane = size * size
    val data = FloatArray(3 * plane)
    val padNorm = padValue / 255f
    // Pre-fill padding; only the letterboxed region is overwritten below.
    data.fill(padNorm)

    val rBase = 0
    val gBase = plane
    val bBase = 2 * plane
    for (dy in 0 until size) {
        val sy = ((dy - padY) / scale)
        if (sy < 0f || sy >= srcHeight) continue
        val iy = sy.toInt().coerceIn(0, srcHeight - 1)
        val rowDst = dy * size
        val rowSrc = iy * srcWidth
        for (dx in 0 until size) {
            val sx = ((dx - padX) / scale)
            if (sx < 0f || sx >= srcWidth) continue
            val ix = sx.toInt().coerceIn(0, srcWidth - 1)
            val argb = pixels[rowSrc + ix]
            val r = ((argb shr 16) and 0xFF) / 255f
            val g = ((argb shr 8) and 0xFF) / 255f
            val b = (argb and 0xFF) / 255f
            val idx = rowDst + dx
            data[rBase + idx] = r
            data[gBase + idx] = g
            data[bBase + idx] = b
        }
    }

    val tensor = OnnxValue(data, longArrayOf(1, 3, size.toLong(), size.toLong()))
    return tensor to LetterboxInfo(scale, padX, padY)
}
