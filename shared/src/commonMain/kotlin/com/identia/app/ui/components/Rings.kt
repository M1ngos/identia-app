package com.identia.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import com.identia.app.core.theme.IdentiaTheme

/**
 * Confidence ring (Face Match). Animates fill 0 → [percent] on first composition,
 * with the percentage in the centre.
 */
@Composable
fun ConfidenceRing(
    percent: Float,
    size: Dp = 148.dp,
    modifier: Modifier = Modifier,
) {
    val track = IdentiaTheme.colors.cardAlt
    val accent = IdentiaTheme.colors.success
    val anim = remember { Animatable(0f) }
    LaunchedEffect(percent) {
        anim.animateTo(percent / 100f, animationSpec = tween(900))
    }
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(Modifier.size(size)) {
            val sw = 14.dp.toPx()
            val inset = sw / 2f
            val arcSize = androidx.compose.ui.geometry.Size(this.size.width - sw, this.size.height - sw)
            val topLeft = androidx.compose.ui.geometry.Offset(inset, inset)
            drawArc(track, 0f, 360f, false, topLeft, arcSize, style = Stroke(sw))
            drawArc(accent, -90f, 360f * anim.value, false, topLeft, arcSize, style = Stroke(sw, cap = StrokeCap.Round))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = formatPercent(anim.value * 100f),
                style = IdentiaTheme.type.numberLg,
                color = accent,
            )
            Text(
                "CONFIDENCE",
                style = IdentiaTheme.type.monoSm,
                color = IdentiaTheme.colors.textSecondaryAlt,
                textAlign = TextAlign.Center,
            )
        }
    }
}

/** Decorative liveness progress ring: full track + accent arc. */
@Composable
fun ProgressRing(size: Dp, modifier: Modifier = Modifier, sweep: Float = 150f) {
    val track = IdentiaTheme.colors.primary.copy(alpha = 0.18f)
    val accent = IdentiaTheme.colors.primary
    Canvas(modifier.size(size)) {
        val sw = 3.dp.toPx()
        val inset = sw / 2f
        val arcSize = androidx.compose.ui.geometry.Size(this.size.width - sw, this.size.height - sw)
        val topLeft = androidx.compose.ui.geometry.Offset(inset, inset)
        drawArc(track, 0f, 360f, false, topLeft, arcSize, style = Stroke(sw))
        drawArc(accent, -60f, sweep, false, topLeft, arcSize, style = Stroke(sw, cap = StrokeCap.Round))
    }
}

private fun formatPercent(value: Float): String {
    val rounded = (value * 10f).toInt() / 10.0
    return "$rounded%"
}
