package com.identia.app.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.identia.app.core.i18n.LocalStrings
import com.identia.app.core.theme.Border
import com.identia.app.core.theme.Card
import com.identia.app.core.theme.IdentiaTheme
import com.identia.app.navigation.Tab
import com.identia.app.state.LocalDemoState
import com.identia.app.ui.components.ChevronRightIcon
import com.identia.app.ui.components.Logo
import com.identia.app.ui.components.RingGlyph
import com.identia.app.ui.components.ScreenScaffold
import com.identia.app.ui.components.SectionLabel
import com.identia.app.ui.components.StatusBanner

@Composable
fun HomeScreen(onModule: (Tab) -> Unit, onSettings: () -> Unit) {
    val demo = LocalDemoState.current
    val strings = LocalStrings.current
    ScreenScaffold {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 18.dp, bottom = 16.dp),
        ) {
            // Brand header — app identity, not personal profile.
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Logo(boxSize = 42.dp, radius = 13.dp, glyphSize = 20.dp)
                Column {
                    Text(
                        "IdentIA",
                        style = IdentiaTheme.type.title,
                        color = IdentiaTheme.colors.textPrimary,
                    )
                    Text(
                        strings.tagline,
                        style = IdentiaTheme.type.monoSm,
                        color = IdentiaTheme.colors.textMuted,
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
            StatusBanner(
                title = strings.identityVerified,
                subtitle = strings.trustScoreValid(demo.trustScore),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(22.dp))
            SectionLabel(strings.modules)
            Spacer(Modifier.height(12.dp))

            // Balanced 2-column module grid.
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ModuleCard(Modifier.weight(1f), strings.moduleIdentityVerification, ModuleIcon.Identity) { onModule(Tab.Verify) }
                ModuleCard(Modifier.weight(1f), strings.moduleFaceAuthentication, ModuleIcon.Face) { onModule(Tab.Face) }
            }
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ModuleCard(Modifier.weight(1f), strings.moduleLogsAudit, ModuleIcon.Logs) { onModule(Tab.Logs) }
                Spacer(Modifier.weight(1f))
            }

            Spacer(Modifier.height(22.dp))
//            SettingsCard(label = strings.settings, onClick = onSettings)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ModuleCard(Modifier.weight(1f), strings.settings, ModuleIcon.Settings, onClick = onSettings)
                Spacer(Modifier.weight(1f))
            }
        }
    }
}

private enum class ModuleIcon { Identity, Face, Logs, Settings }

@Composable
private fun ModuleCard(
    modifier: Modifier = Modifier,
    title: String,
    icon: ModuleIcon,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .height(108.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Card)
            .border(1.dp, Border, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        ModuleGlyph(icon)
        Text(
            title,
            style = IdentiaTheme.type.bodySemi,
            color = IdentiaTheme.colors.textPrimary,
        )
    }
}

@Composable
private fun ModuleGlyph(icon: ModuleIcon) {
    val purple = IdentiaTheme.colors.primary
    val vector = when (icon) {
        ModuleIcon.Identity -> Icons.Outlined.Badge
        ModuleIcon.Face -> Icons.Outlined.Face
        ModuleIcon.Logs -> Icons.Outlined.Description
        ModuleIcon.Settings -> Icons.Outlined.Settings
    }
    Box(
        Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(11.dp))
            .background(purple.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(vector, contentDescription = null, tint = purple, modifier = Modifier.size(22.dp))
    }
}

@Composable
private fun SettingsCard(label: String, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Card)
            .border(1.dp, Border, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        RingGlyph(size = 24.dp, color = IdentiaTheme.colors.primary, strokeWidth = 2.dp)

        Text(
            label,
            style = IdentiaTheme.type.bodySemi,
            color = IdentiaTheme.colors.textPrimary,
            modifier = Modifier.weight(1f),
        )
        ChevronRightIcon(size = 16.dp, color = IdentiaTheme.colors.textMuted)
    }
}
