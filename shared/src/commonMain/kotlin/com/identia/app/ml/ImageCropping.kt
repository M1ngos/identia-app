package com.identia.app.ml

import kotlin.math.roundToInt

/**
 * Crop a rectangular region out of a packed-ARGB [DecodedImage].
 *
 * The crop is taken in the source image's pixel coordinates — the same space
 * [Detection.box] lives in — so a detector box can be fed straight in. The box is
 * clamped to the image bounds and may be inflated by [padFraction] of its own
 * size on every edge, which gives OCR a little margin around tight field boxes
 * (text recognizers do better with a few pixels of quiet space).
 *
 * @param padFraction fraction of box width/height to add on each side (0 = none).
 * @return the cropped [DecodedImage], or null if the box has no area on-screen.
 */
fun DecodedImage.crop(box: BoundingBox, padFraction: Float = 0.06f): DecodedImage? {
    val padX = box.width * padFraction
    val padY = box.height * padFraction
    val left = (box.x1 - padX).roundToInt().coerceIn(0, width)
    val top = (box.y1 - padY).roundToInt().coerceIn(0, height)
    val right = (box.x2 + padX).roundToInt().coerceIn(0, width)
    val bottom = (box.y2 + padY).roundToInt().coerceIn(0, height)

    val cropW = right - left
    val cropH = bottom - top
    if (cropW <= 0 || cropH <= 0) return null

    val out = IntArray(cropW * cropH)
    for (y in 0 until cropH) {
        val srcRow = (top + y) * width + left
        pixels.copyInto(out, y * cropW, srcRow, srcRow + cropW)
    }
    return DecodedImage(out, cropW, cropH)
}
