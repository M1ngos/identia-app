package com.identia.app.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.identia.app.core.theme.Bg
import com.identia.app.core.theme.IdentiaTheme
import com.identia.app.ui.components.DashedDemoButton
import com.identia.app.ui.components.Logo
import com.identia.app.ui.components.PrimaryButton
import com.identia.app.ui.components.ScreenScaffold
import com.identia.app.ui.components.SecondaryButton

@Composable
fun AuthEntryScreen(onEnter: () -> Unit) {
    val bg = Brush.verticalGradient(listOf(androidx.compose.ui.graphics.Color(0xFF16121F), Bg))
    ScreenScaffold(brush = bg) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Logo(boxSize = 88.dp, radius = 26.dp, glyphSize = 40.dp, animatedRing = true)
                Spacer(Modifier.height(26.dp))
                Text("IdentIA", style = IdentiaTheme.type.brand, color = IdentiaTheme.colors.textPrimary)
                Spacer(Modifier.height(8.dp))
                Text(
                    "VERIFY · AUTHENTICATE · TRUST",
                    style = IdentiaTheme.type.monoTag,
                    color = IdentiaTheme.colors.primary,
                )
                Spacer(Modifier.height(38.dp))
                PrimaryButton("Log In", onClick = onEnter)
                Spacer(Modifier.height(12.dp))
                SecondaryButton("Continue as Guest", onClick = onEnter)
                Spacer(Modifier.height(12.dp))
                DashedDemoButton("⌁ ENTER DEMO MODE", onClick = onEnter)
            }
            Text(
                "END-TO-END ENCRYPTED · v2.4.0-demo",
                style = IdentiaTheme.type.monoSm.copy(letterSpacing = androidx.compose.ui.unit.TextUnit.Unspecified),
                color = IdentiaTheme.colors.textMutedAlt,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 26.dp),
            )
        }
    }
}
