package com.identia.app.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.identia.app.core.theme.CardInput
import com.identia.app.core.theme.IdentiaTheme
import com.identia.app.core.theme.Success
import com.identia.app.state.LocalDemoState
import com.identia.app.ui.components.DangerButton
import com.identia.app.ui.components.IdentiaCard
import com.identia.app.ui.components.IdentiaToggle
import com.identia.app.ui.components.RowDivider
import com.identia.app.ui.components.ScreenScaffold
import com.identia.app.ui.components.SectionLabel
import com.identia.app.ui.components.TopBarWithBack
import com.identia.app.ui.components.pulse

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val demo = LocalDemoState.current
    ScreenScaffold {
        TopBarWithBack("Settings", onBack)
        Column(
            Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 4.dp),
        ) {
            SectionLabel("API Configuration")
            Spacer(Modifier.height(9.dp))
            IdentiaCard(Modifier.fillMaxWidth(), radius = 14) {
                Column(Modifier.padding(14.dp)) {
                    Text("Endpoint (demo)", style = IdentiaTheme.type.body, color = IdentiaTheme.colors.textSecondaryAlt)
                    Spacer(Modifier.height(7.dp))
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(CardInput)
                            .border(1.dp, IdentiaTheme.colors.primary.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("api.identia.io/v2", style = IdentiaTheme.type.mono, color = IdentiaTheme.colors.accentSoft)
                        Spacer(Modifier.weight(1f))
                        Box(Modifier.size(width = 7.dp, height = 14.dp).background(IdentiaTheme.colors.primary).pulse(0.2f, 1f, 1100))
                    }
                    Spacer(Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                        Box(Modifier.size(7.dp).clip(CircleShape).background(Success))
                        Text("Connected · 38ms", style = IdentiaTheme.type.mono, color = Success)
                    }
                }
            }
            Spacer(Modifier.height(18.dp))
            SectionLabel("Preferences")
            Spacer(Modifier.height(9.dp))
            IdentiaCard(Modifier.fillMaxWidth(), radius = 14) {
                ToggleRow("Theme", "Dark mode", demo.darkMode) { demo.darkMode = it }
                RowDivider()
                ToggleRow("Biometric unlock", "Face ID on launch", demo.biometricUnlock) { demo.biometricUnlock = it }
                RowDivider()
                ToggleRow("Simulate failure", "Force verification to fail", demo.simulateFailure) { demo.simulateFailure = it }
            }
            Spacer(Modifier.height(18.dp))
            SectionLabel("Demo Data")
            Spacer(Modifier.height(9.dp))
            DangerButton("Reset Demo Data", onClick = {
                demo.simulateFailure = false
                demo.biometricUnlock = true
                demo.darkMode = true
            }) {
                Box(Modifier.size(13.dp).clip(CircleShape).border(2.dp, IdentiaTheme.colors.errorSoft, CircleShape))
            }
            Spacer(Modifier.height(22.dp))
        }
    }
}

@Composable
private fun ToggleRow(title: String, subtitle: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, style = IdentiaTheme.type.bodyStrong, color = IdentiaTheme.colors.textPrimary)
            Text(subtitle, style = IdentiaTheme.type.mono, color = IdentiaTheme.colors.textSecondaryAlt)
        }
        IdentiaToggle(checked = checked, onCheckedChange = onChange)
    }
}
