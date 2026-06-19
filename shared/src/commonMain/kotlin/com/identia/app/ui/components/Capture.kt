package com.identia.app.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Camera shutter button: white ring with a solid white inner disc. */
@Composable
fun ShutterButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(62.dp)
            .clip(CircleShape)
            .border(3.dp, Color.White.copy(alpha = 0.85f), CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Box(Modifier.size(48.dp).clip(CircleShape).background(Color.White))
    }
}

/**
 * Face silhouette guide: outer oval (dashed/solid, optionally glowing) with an
 * inner face outline (eyes + mouth). Used on Selfie, Liveness, Face camera/scan.
 */
@Composable
fun FaceOvalGuide(
    width: Dp,
    height: Dp,
    color: Color,
    modifier: Modifier = Modifier,
    dashed: Boolean = true,
    strokeWidth: Dp = 2.5.dp,
) {
    Canvas(modifier.size(width, height)) {
        val sw = strokeWidth.toPx()
        val outer = Rect(Offset(sw / 2f, sw / 2f), Size(size.width - sw, size.height - sw))
        val effect = if (dashed) PathEffect.dashPathEffect(floatArrayOf(10f, 8f)) else null
        drawOval(color = color, topLeft = outer.topLeft, size = outer.size, style = Stroke(sw, pathEffect = effect))

        // Inner face outline
        val iw = size.width * 0.56f
        val ih = size.height * 0.56f
        val ix = (size.width - iw) / 2f
        val iy = (size.height - ih) / 2f
        val inner = Color.White.copy(alpha = 0.18f)
        drawOval(color = inner, topLeft = Offset(ix, iy), size = Size(iw, ih), style = Stroke(2f))

        // Eyes
        val eyeY = iy + ih * 0.4f
        val eyeR = size.width * 0.03f
        drawCircle(Color.White.copy(alpha = 0.25f), eyeR, Offset(ix + iw * 0.32f, eyeY))
        drawCircle(Color.White.copy(alpha = 0.25f), eyeR, Offset(ix + iw * 0.68f, eyeY))

        // Mouth
        val mouthRect = Rect(
            Offset(ix + iw * 0.38f, iy + ih * 0.6f),
            Size(iw * 0.24f, ih * 0.18f),
        )
        drawArc(
            color = Color.White.copy(alpha = 0.22f),
            startAngle = 20f,
            sweepAngle = 140f,
            useCenter = false,
            topLeft = mouthRect.topLeft,
            size = mouthRect.size,
            style = Stroke(2f, cap = StrokeCap.Round),
        )
    }
}

/** Four L-shaped corner brackets (camera framing). */
@Composable
fun CornerBrackets(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier.fillMaxSize()) {
        val pad = 14.dp.toPx()
        val len = 24.dp.toPx()
        val bw = 2.5.dp.toPx()
        fun corner(x: Float, y: Float, dx: Float, dy: Float) {
            drawLine(color, Offset(x, y), Offset(x + dx, y), bw, StrokeCap.Round)
            drawLine(color, Offset(x, y), Offset(x, y + dy), bw, StrokeCap.Round)
        }
        corner(pad, pad, len, len)
        corner(size.width - pad, pad, -len, len)
        corner(pad, size.height - pad, len, -len)
        corner(size.width - pad, size.height - pad, -len, -len)
    }
}

/**
 * ID document viewfinder: animated scan line, four corner brackets, and a
 * dashed ID-card outline in the centre.
 */
@Composable
fun ViewfinderFrame(
    accent: Color,
    modifier: Modifier = Modifier,
) {
    val transition = rememberInfiniteTransition()
    val scan by transition.animateFloat(
        0f, 1f, infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Restart),
    )
    Canvas(modifier.fillMaxSize()) {
        val pad = 14.dp.toPx()
        val len = 26.dp.toPx()
        val bw = 2.5.dp.toPx()

        fun corner(x: Float, y: Float, dx: Float, dy: Float) {
            drawLine(accent, Offset(x, y), Offset(x + dx, y), bw, StrokeCap.Round)
            drawLine(accent, Offset(x, y), Offset(x, y + dy), bw, StrokeCap.Round)
        }
        corner(pad, pad, len, len)                                  // top-left
        corner(size.width - pad, pad, -len, len)                    // top-right
        corner(pad, size.height - pad, len, -len)                   // bottom-left
        corner(size.width - pad, size.height - pad, -len, -len)     // bottom-right

        // Scan line
        val lineH = 60.dp.toPx()
        val travel = size.height - lineH
        val y = travel * scan
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Transparent, accent.copy(alpha = 0.35f), Color.Transparent),
                startY = y,
                endY = y + lineH,
            ),
            topLeft = Offset(pad, y),
            size = Size(size.width - pad * 2, lineH),
        )

        // Dashed ID card outline (centred)
        val cardW = size.width * 0.66f
        val cardH = cardW * 0.62f
        val cx = (size.width - cardW) / 2f
        val cy = (size.height - cardH) / 2f
        drawRoundRect(
            color = Color.White.copy(alpha = 0.28f),
            topLeft = Offset(cx, cy),
            size = Size(cardW, cardH),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx()),
            style = Stroke(2.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f))),
        )
    }
}
