package com.identia.app.feature.verify

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.identia.app.core.theme.BgPure
import com.identia.app.core.theme.Card
import com.identia.app.core.theme.IdentiaTheme
import com.identia.app.core.theme.Success
import com.identia.app.state.LocalDemoState
import com.identia.app.ui.components.CheckIcon
import com.identia.app.ui.components.FaceOvalGuide
import com.identia.app.ui.components.ProgressRing
import com.identia.app.ui.components.ScreenScaffold
import com.identia.app.ui.components.SegmentedProgress
import com.identia.app.ui.components.TopBarWithBack
import com.identia.app.ui.components.pulse
import kotlinx.coroutines.delay

@Composable
fun LivenessScreen(onBack: () -> Unit, onComplete: (failed: Boolean) -> Unit) {
    val demo = LocalDemoState.current
    var step by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        delay(1300); step = 1   // blink
        delay(1300); step = 2   // smile
        delay(1500); step = 3   // turn head
        delay(500)
        onComplete(demo.simulateFailure)
    }

    ScreenScaffold(background = BgPure) {
        TopBarWithBack("Liveness Check", onBack, titleColor = Color.White)
        SegmentedProgress(
            total = 4, active = 3,
            modifier = Modifier.padding(horizontal = 22.dp).padding(bottom = 12.dp),
        )
        Column(
            Modifier.fillMaxSize().padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(Modifier.size(188.dp), contentAlignment = Alignment.Center) {
                ProgressRing(size = 188.dp)
                Halo()
                FaceOvalGuide(width = 130.dp, height = 160.dp, color = IdentiaTheme.colors.primary.copy(alpha = 0.5f), strokeWidth = 2.dp)
            }
            Spacer(Modifier.height(12.dp))
            Text(
                "Slowly turn your head →",
                style = IdentiaTheme.type.title,
                color = IdentiaTheme.colors.textPrimary,
                modifier = Modifier.pulse(0.5f, 1f, 1600),
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Keep your face inside the circle",
                style = IdentiaTheme.type.body,
                color = IdentiaTheme.colors.textSecondaryAlt,
            )
            Spacer(Modifier.height(26.dp))
            CheckItem("Blink detected", done = step >= 1)
            Spacer(Modifier.height(10.dp))
            CheckItem("Smile detected", done = step >= 2)
            Spacer(Modifier.height(10.dp))
            CheckItem(if (step >= 3) "Turn head detected" else "Turn head — in progress", done = step >= 3)
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun Halo() {
    val transition = rememberInfiniteTransition()
    val scale by transition.animateFloat(0.85f, 1.25f, infiniteRepeatable(tween(2000)))
    val alpha by transition.animateFloat(0.7f, 0f, infiniteRepeatable(tween(2000)))
    Box(
        Modifier
            .size(208.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale; this.alpha = alpha }
            .border(1.dp, IdentiaTheme.colors.primary.copy(alpha = 0.4f), CircleShape),
    )
}

@Composable
private fun CheckItem(text: String, done: Boolean) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (done) Success.copy(alpha = 0.08f) else Card)
            .border(
                1.dp,
                if (done) Success.copy(alpha = 0.22f) else IdentiaTheme.colors.border,
                RoundedCornerShape(12.dp),
            )
            .padding(horizontal = 14.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(11.dp),
    ) {
        if (done) {
            Box(
                Modifier.size(20.dp).clip(CircleShape).background(Success.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center,
            ) { CheckIcon(size = 11.dp, color = Success, strokeWidth = 2.dp) }
            Text(text, style = IdentiaTheme.type.body, color = Color(0xFFCFEEDE))
        } else {
            Box(Modifier.size(20.dp).clip(CircleShape).border(2.dp, IdentiaTheme.colors.primary, CircleShape).pulse(0.4f, 1f, 1200))
            Text(text, style = IdentiaTheme.type.body, color = IdentiaTheme.colors.textSecondary)
        }
    }
}
