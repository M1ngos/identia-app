package com.identia.app.feature.face

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.identia.app.core.i18n.LocalStrings
import com.identia.app.core.theme.BgPure
import com.identia.app.core.theme.Card
import com.identia.app.core.theme.IdentiaTheme
import com.identia.app.ui.components.FaceOvalGuide
import com.identia.app.ui.components.ScreenScaffold
import com.identia.app.ui.components.pulse
import kotlinx.coroutines.delay

@Composable
fun FaceScanningScreen(onComplete: () -> Unit) {
    val strings = LocalStrings.current
    LaunchedEffect(Unit) {
        delay(2200)
        onComplete()
    }
    ScreenScaffold(background = BgPure) {
        // Header (no back affordance during scan)
        Text(
            strings.scanningFace,
            style = IdentiaTheme.type.titleSm,
            color = Color.White,
            modifier = Modifier.padding(start = 18.dp + 34.dp, top = 8.dp, bottom = 12.dp),
        )
        Column(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Brush.radialGradient(listOf(Color(0xFF1A1530), BgPure))),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            ScanFace()
            Spacer(Modifier.height(22.dp))
            Text(
                strings.analyzingDataPoints,
                style = IdentiaTheme.type.monoLabel.copy(letterSpacing = 0.08.em),
                color = IdentiaTheme.colors.accentSoft,
                modifier = Modifier.pulse(0.45f, 1f, 1400),
            )
        }
        Box(Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(top = 18.dp, bottom = 26.dp)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Card)
                    .border(1.dp, IdentiaTheme.colors.border, RoundedCornerShape(14.dp))
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(Modifier.size(8.dp).clip(CircleShape).background(IdentiaTheme.colors.primary).pulse(0.3f, 1f, 1000))
                Text(strings.holdStill, style = IdentiaTheme.type.bodyStrong, color = IdentiaTheme.colors.textSecondaryAlt)
            }
        }
    }
}

@Composable
private fun ScanFace() {
    val transition = rememberInfiniteTransition()
    val sweep by transition.animateFloat(
        0f, 1f, infiniteRepeatable(tween(1800, easing = LinearEasing), RepeatMode.Restart),
    )
    Box(Modifier.size(width = 175.dp, height = 215.dp), contentAlignment = Alignment.Center) {
        FaceOvalGuide(
            width = 170.dp, height = 210.dp,
            color = IdentiaTheme.colors.primary, dashed = false,
        )
        // Scan sweep
        val travel = 210.dp
        Box(
            Modifier
                .offset(y = (travel * sweep) - 105.dp)
                .size(width = 150.dp, height = 36.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, IdentiaTheme.colors.primary.copy(alpha = 0.55f), Color.Transparent),
                    ),
                ),
        )
        // Mesh dots
        MeshDot(-44.dp, -52.dp, 1000)
        MeshDot(44.dp, -52.dp, 1200)
        MeshDot(0.dp, 6.dp, 900)
        MeshDot(-34.dp, 56.dp, 1100)
        MeshDot(34.dp, 56.dp, 1300)
    }
}

@Composable
private fun MeshDot(x: androidx.compose.ui.unit.Dp, y: androidx.compose.ui.unit.Dp, durationMs: Int) {
    Box(
        Modifier
            .offset(x = x, y = y)
            .size(5.dp)
            .clip(CircleShape)
            .background(IdentiaTheme.colors.primary)
            .pulse(0.3f, 1f, durationMs),
    )
}
