package com.identia.app.feature.face

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.identia.app.core.theme.Bg
import com.identia.app.core.theme.IdentiaTheme
import com.identia.app.core.theme.Success
import com.identia.app.ui.components.CheckIcon
import com.identia.app.ui.components.ConfidenceRing
import com.identia.app.ui.components.DetailRow
import com.identia.app.ui.components.DividedCard
import com.identia.app.ui.components.PrimaryButton
import com.identia.app.ui.components.RowDivider
import com.identia.app.ui.components.ScreenScaffold

@Composable
fun FaceMatchScreen(confidence: Float, onContinue: () -> Unit) {
    val bg = Brush.verticalGradient(listOf(Color(0xFF0F1D18), Bg))
    ScreenScaffold(brush = bg) {
        Column(
            Modifier.fillMaxSize().padding(horizontal = 26.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(14.dp))
            Box(
                Modifier
                    .size(84.dp)
                    .clip(CircleShape)
                    .background(Success.copy(alpha = 0.14f))
                    .border(2.dp, Success.copy(alpha = 0.45f), CircleShape),
                contentAlignment = Alignment.Center,
            ) { CheckIcon(size = 30.dp, color = Success, strokeWidth = 3.5.dp) }
            Spacer(Modifier.height(18.dp))
            Text("Match Confirmed", style = IdentiaTheme.type.headingMd, color = IdentiaTheme.colors.textPrimary)
            Spacer(Modifier.height(7.dp))
            Text("Welcome back, Alex.", style = IdentiaTheme.type.body, color = IdentiaTheme.colors.textSecondaryAlt, textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))
            ConfidenceRing(percent = confidence, size = 148.dp)
            Spacer(Modifier.height(16.dp))
            DividedCard(Modifier.fillMaxWidth()) {
                DetailRow("Threshold", "85.0%")
                RowDivider()
                DetailRow("Liveness", "Passed", valueColor = Success)
                RowDivider()
                DetailRow("Latency", "412 ms")
            }
            Spacer(Modifier.weight(1f))
            PrimaryButton("Continue", onClick = onContinue)
            Spacer(Modifier.height(22.dp))
        }
    }
}
