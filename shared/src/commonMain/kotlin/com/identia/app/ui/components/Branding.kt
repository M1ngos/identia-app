package com.identia.app.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.identia.app.core.theme.Gradients
import com.identia.app.core.theme.IdentiaTheme
import com.identia.app.core.theme.Success

/** Gradient rounded-square logo with concentric-ring glyph and optional pulsing halo. */
@Composable
fun Logo(
    boxSize: Dp = 54.dp,
    radius: Dp = 16.dp,
    glyphSize: Dp = 24.dp,
    animatedRing: Boolean = false,
) {
    Box(contentAlignment = Alignment.Center) {
        if (animatedRing) {
            val transition = rememberInfiniteTransition()
            val scale by transition.animateFloat(0.85f, 1.25f, infiniteRepeatable(tween(2400)))
            val alpha by transition.animateFloat(0.7f, 0f, infiniteRepeatable(tween(2400)))
            Box(
                Modifier
                    .size(boxSize)
                    .graphicsLayer { scaleX = scale; scaleY = scale; this.alpha = alpha }
                    .border(2.dp, IdentiaTheme.colors.primary.copy(alpha = 0.6f), RoundedCornerShape(radius)),
            )
        }
        Box(
            modifier = Modifier
                .size(boxSize)
                .glow(IdentiaTheme.colors.primary.copy(alpha = 0.5f), 28.dp, RoundedCornerShape(radius))
                .clip(RoundedCornerShape(radius))
                .background(Gradients.purple),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Fingerprint,
                contentDescription = "IdentIA logo",
                tint = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.95f),
                modifier = Modifier.size(glyphSize),
            )
        }
    }
}

/** Home "Identity Verified" success banner. */
@Composable
fun StatusBanner(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Gradients.successBanner)
            .border(1.dp, Success.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Success.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center,
        ) {
            CheckIcon(size = 16.dp, color = Success)
        }
        Column(Modifier.padding(start = 12.dp)) {
            Text(title, style = IdentiaTheme.type.bodySemi, color = IdentiaTheme.colors.textPrimary)
            Text(subtitle, style = IdentiaTheme.type.mono, color = IdentiaTheme.colors.textSecondaryAlt)
        }
    }
}
