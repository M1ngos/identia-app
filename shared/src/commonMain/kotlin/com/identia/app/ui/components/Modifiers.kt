package com.identia.app.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Looping opacity pulse (CSS `idPulse`). */
@Composable
fun Modifier.pulse(min: Float = 0.35f, max: Float = 1f, durationMs: Int = 1400): Modifier {
    val transition = rememberInfiniteTransition()
    val alpha by transition.animateFloat(
        initialValue = max,
        targetValue = min,
        animationSpec = infiniteRepeatable(tween(durationMs), RepeatMode.Reverse),
    )
    return this.graphicsLayer { this.alpha = alpha }
}

/** Approximated colored glow shadow used on buttons / logo / accents. */
fun Modifier.glow(
    color: Color,
    radius: Dp = 24.dp,
    shape: Shape = RoundedCornerShape(14.dp),
): Modifier = this.shadow(elevation = radius, shape = shape, ambientColor = color, spotColor = color)

/** Rounded dashed border (CSS `border:Npx dashed`). */
fun Modifier.dashedBorder(
    color: Color,
    width: Dp = 1.dp,
    radius: Dp = 14.dp,
    on: Dp = 6.dp,
    off: Dp = 4.dp,
): Modifier = this.drawBehind {
    drawRoundRect(
        color = color,
        cornerRadius = CornerRadius(radius.toPx()),
        style = Stroke(
            width = width.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(on.toPx(), off.toPx())),
        ),
    )
}
