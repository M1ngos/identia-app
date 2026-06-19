package com.identia.app.feature.verify

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.identia.app.core.theme.Bg
import com.identia.app.core.theme.IdentiaTheme
import com.identia.app.core.theme.Success
import com.identia.app.core.theme.Warning
import androidx.compose.ui.graphics.Color
import com.identia.app.ui.components.CheckIcon
import com.identia.app.ui.components.CrossIcon
import com.identia.app.ui.components.DetailRow
import com.identia.app.ui.components.DividedCard
import com.identia.app.ui.components.IssueRow
import com.identia.app.ui.components.PrimaryButton
import com.identia.app.ui.components.RowDivider
import com.identia.app.ui.components.ScreenScaffold
import com.identia.app.ui.components.SecondaryButton

@Composable
fun VerifyResultScreen(failed: Boolean, onDone: () -> Unit, onRetry: () -> Unit) {
    if (failed) FailedResult(onRetry) else VerifiedResult(onDone)
}

@Composable
private fun VerifiedResult(onDone: () -> Unit) {
    val bg = Brush.verticalGradient(listOf(Color(0xFF0F1D18), Bg))
    ScreenScaffold(brush = bg) {
        Column(
            Modifier.fillMaxSize().padding(horizontal = 26.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(18.dp))
            ResultIcon(color = Success, halo = true) { CheckIcon(size = 34.dp, color = Success, strokeWidth = 3.5.dp) }
            Spacer(Modifier.height(22.dp))
            Text("Identity Verified", style = IdentiaTheme.type.headingLg, color = IdentiaTheme.colors.textPrimary)
            Spacer(Modifier.height(8.dp))
            Text(
                "Your document and biometrics matched successfully.",
                style = IdentiaTheme.type.body,
                color = IdentiaTheme.colors.textSecondaryAlt,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(22.dp))
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("98", style = IdentiaTheme.type.numberHero, color = Success)
                Text(
                    "/100 trust score",
                    style = IdentiaTheme.type.mono,
                    color = IdentiaTheme.colors.textSecondaryAlt,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }
            Spacer(Modifier.height(20.dp))
            DividedCard(Modifier.fillMaxWidth()) {
                DetailRow("Name", "Alex Mercer")
                RowDivider()
                DetailRow("Document", "Passport ····7421")
                RowDivider()
                DetailRow("Face match", "99.2%", valueColor = Success)
            }
            Spacer(Modifier.weight(1f))
            PrimaryButton("Done", onClick = onDone)
            Spacer(Modifier.height(22.dp))
        }
    }
}

@Composable
private fun FailedResult(onRetry: () -> Unit) {
    val bg = Brush.verticalGradient(listOf(Color(0xFF1F1014), Bg))
    ScreenScaffold(brush = bg) {
        Column(
            Modifier.fillMaxSize().padding(horizontal = 26.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(18.dp))
            ResultIcon(color = IdentiaTheme.colors.error, halo = false) {
                CrossIcon(size = 30.dp, color = IdentiaTheme.colors.error)
            }
            Spacer(Modifier.height(22.dp))
            Text("Verification Failed", style = IdentiaTheme.type.headingLg, color = IdentiaTheme.colors.textPrimary)
            Spacer(Modifier.height(8.dp))
            Text(
                "We couldn't confirm a match. Please review the issues below.",
                style = IdentiaTheme.type.body,
                color = IdentiaTheme.colors.textSecondaryAlt,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(24.dp))
            DividedCard(Modifier.fillMaxWidth()) {
                IssueRow("Document photo too blurry", dotColor = IdentiaTheme.colors.error)
                RowDivider()
                IssueRow("Face match below threshold (61%)", dotColor = IdentiaTheme.colors.error)
                RowDivider()
                IssueRow("Liveness inconclusive", dotColor = Warning)
            }
            Spacer(Modifier.weight(1f))
            PrimaryButton("Try Again", onClick = onRetry)
            Spacer(Modifier.height(12.dp))
            SecondaryButton("Contact Support", onClick = {})
            Spacer(Modifier.height(22.dp))
        }
    }
}

@Composable
private fun ResultIcon(color: Color, halo: Boolean, content: @Composable () -> Unit) {
    Box(contentAlignment = Alignment.Center) {
        Box(
            Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.13f))
                .border(2.dp, color.copy(alpha = 0.45f), CircleShape),
            contentAlignment = Alignment.Center,
            content = { content() },
        )
    }
}
