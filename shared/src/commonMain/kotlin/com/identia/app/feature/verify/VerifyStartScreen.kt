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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.identia.app.core.theme.Card
import com.identia.app.core.theme.IdentiaTheme
import com.identia.app.ui.components.InfoPill
import com.identia.app.ui.components.PrimaryButton
import com.identia.app.ui.components.ScreenScaffold
import com.identia.app.ui.components.TopBarWithBack

@Composable
fun VerifyStartScreen(onBack: () -> Unit, onStart: () -> Unit) {
    ScreenScaffold {
        TopBarWithBack("Verify Identity", onBack)
        Column(
            Modifier.fillMaxSize().padding(horizontal = 24.dp),
        ) {
            Spacer(Modifier.height(8.dp))
            Box(
                Modifier
                    .size(72.dp)
                    .background(IdentiaTheme.colors.primary.copy(alpha = 0.12f), RoundedCornerShape(22.dp))
                    .border(1.dp, IdentiaTheme.colors.primary.copy(alpha = 0.35f), RoundedCornerShape(22.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Box(Modifier.size(30.dp).border(2.5.dp, IdentiaTheme.colors.primary, RoundedCornerShape(8.dp)))
            }
            Spacer(Modifier.height(20.dp))
            Text(
                "Let's confirm it's really you",
                style = IdentiaTheme.type.headingMd,
                color = IdentiaTheme.colors.textPrimary,
            )
            Spacer(Modifier.height(10.dp))
            Text(
                "A quick three-step check. Have your government ID ready and find good lighting.",
                style = IdentiaTheme.type.body,
                color = IdentiaTheme.colors.textSecondaryAlt,
            )
            Spacer(Modifier.height(24.dp))
            StepRow(1, "Scan your ID document")
            Spacer(Modifier.height(12.dp))
            StepRow(2, "Take a quick selfie")
            Spacer(Modifier.height(12.dp))
            StepRow(3, "Pass the liveness check")
            Spacer(Modifier.weight(1f))
            InfoPill("≈ 60 seconds · secure")
            Spacer(Modifier.height(14.dp))
            PrimaryButton("Start Verification", onClick = onStart)
            Spacer(Modifier.height(22.dp))
        }
    }
}

@Composable
private fun StepRow(number: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(13.dp)) {
        Box(
            Modifier
                .size(32.dp)
                .background(Card, RoundedCornerShape(10.dp))
                .border(1.dp, IdentiaTheme.colors.border, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text("$number", style = IdentiaTheme.type.monoLabel, color = IdentiaTheme.colors.primary)
        }
        Text(text, style = IdentiaTheme.type.body, color = IdentiaTheme.colors.textPrimary)
    }
}
