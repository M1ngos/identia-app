package com.identia.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Canvas-drawn glyphs. The HTML mock builds icons out of CSS borders, so these
 * are line-based primitives rather than a glyph font.
 */

@Composable
fun CheckIcon(size: Dp, color: Color, strokeWidth: Dp = 2.5.dp) {
    Canvas(Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val sw = strokeWidth.toPx()
        drawLine(
            color = color,
            start = Offset(w * 0.18f, h * 0.52f),
            end = Offset(w * 0.42f, h * 0.74f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = color,
            start = Offset(w * 0.42f, h * 0.74f),
            end = Offset(w * 0.82f, h * 0.28f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
    }
}

@Composable
fun CrossIcon(size: Dp, color: Color, strokeWidth: Dp = 3.5.dp) {
    Canvas(Modifier.size(size)) {
        val w = this.size.width
        val sw = strokeWidth.toPx()
        drawLine(color, Offset(w * 0.2f, w * 0.2f), Offset(w * 0.8f, w * 0.8f), sw, StrokeCap.Round)
        drawLine(color, Offset(w * 0.8f, w * 0.2f), Offset(w * 0.2f, w * 0.8f), sw, StrokeCap.Round)
    }
}

@Composable
fun ChevronLeftIcon(size: Dp, color: Color, strokeWidth: Dp = 2.dp) {
    Canvas(Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val sw = strokeWidth.toPx()
        drawLine(color, Offset(w * 0.6f, h * 0.22f), Offset(w * 0.34f, h * 0.5f), sw, StrokeCap.Round)
        drawLine(color, Offset(w * 0.34f, h * 0.5f), Offset(w * 0.6f, h * 0.78f), sw, StrokeCap.Round)
    }
}

@Composable
fun RingGlyph(size: Dp, color: Color, strokeWidth: Dp = 2.dp) {
    Canvas(Modifier.size(size)) {
        val sw = strokeWidth.toPx()
        val outer = (this.size.minDimension - sw) / 2f
        val center = Offset(this.size.width / 2f, this.size.height / 2f)
        drawCircle(color, radius = outer, center = center, style = Stroke(sw))
        drawCircle(color, radius = outer * 0.42f, center = center, style = Stroke(sw))
    }
}
