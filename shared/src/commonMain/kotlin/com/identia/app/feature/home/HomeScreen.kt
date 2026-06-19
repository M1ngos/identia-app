package com.identia.app.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.identia.app.core.theme.Border
import com.identia.app.core.theme.Card
import com.identia.app.core.theme.Gradients
import com.identia.app.core.theme.IdentiaTheme
import com.identia.app.navigation.Tab
import com.identia.app.state.LocalDemoState
import com.identia.app.ui.components.RingGlyph
import com.identia.app.ui.components.ScreenScaffold
import com.identia.app.ui.components.SectionLabel
import com.identia.app.ui.components.StatusBanner

@Composable
fun HomeScreen(onModule: (Tab) -> Unit, onSettings: () -> Unit) {
    val demo = LocalDemoState.current
    ScreenScaffold {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 14.dp, bottom = 16.dp),
        ) {
            // Header
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text("Welcome back", style = IdentiaTheme.type.body, color = IdentiaTheme.colors.textSecondaryAlt)
                    Text(demo.userName, style = IdentiaTheme.type.title.copy(fontSize = 21.sp), color = IdentiaTheme.colors.textPrimary)
                }
                Avatar(demo.initials, size = 42.dp)
            }
            Spacer(Modifier.height(18.dp))
            StatusBanner(
                title = "Identity Verified",
                subtitle = "Trust score ${demo.trustScore} · valid",
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(18.dp))
            SectionLabel("Modules")
            Spacer(Modifier.height(10.dp))

            // 2-col grid
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(11.dp)) {
                ModuleCard(Modifier.weight(1f), "Identity\nVerification") { onModule(Tab.Verify) }
                ModuleCard(Modifier.weight(1f), "Face\nAuthentication", icon = ModuleIcon.Circle) { onModule(Tab.Face) }
            }
            Spacer(Modifier.height(11.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(11.dp)) {
                ModuleCard(Modifier.weight(1f), "Profile", icon = ModuleIcon.FilledCircle) { onModule(Tab.Profile) }
                ModuleCard(Modifier.weight(1f), "Logs / Audit", icon = ModuleIcon.Lines) { onModule(Tab.Logs) }
            }
            Spacer(Modifier.height(11.dp))
            SettingsCard(onClick = onSettings)
        }
    }
}

@Composable
private fun Avatar(initials: String, size: androidx.compose.ui.unit.Dp) {
    Box(
        Modifier
            .size(size)
            .clip(CircleShape)
            .background(Gradients.avatar)
            .border(1.dp, IdentiaTheme.colors.border, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(initials, style = IdentiaTheme.type.titleSm, color = IdentiaTheme.colors.accentSoft)
    }
}

private enum class ModuleIcon { Square, Circle, FilledCircle, Lines }

@Composable
private fun ModuleCard(
    modifier: Modifier = Modifier,
    title: String,
    icon: ModuleIcon = ModuleIcon.Square,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .height(96.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Card)
            .border(1.dp, Border, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        ModuleGlyph(icon)
        Text(title, style = IdentiaTheme.type.bodySemi.copy(fontSize = 13.sp), color = IdentiaTheme.colors.textPrimary)
    }
}

@Composable
private fun ModuleGlyph(icon: ModuleIcon) {
    val purple = IdentiaTheme.colors.primary
    when (icon) {
        ModuleIcon.Square -> Box(Modifier.size(28.dp).border(2.dp, purple, RoundedCornerShape(8.dp)))
        ModuleIcon.Circle -> Box(Modifier.size(28.dp).border(2.dp, purple, CircleShape))
        ModuleIcon.FilledCircle -> Box(
            Modifier.size(28.dp).clip(CircleShape).background(purple.copy(alpha = 0.18f)).border(2.dp, purple, CircleShape),
        )
        ModuleIcon.Lines -> Column(
            Modifier.size(28.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterVertically),
        ) {
            Box(Modifier.fillMaxWidth().height(2.5.dp).clip(RoundedCornerShape(2.dp)).background(purple))
            Box(Modifier.fillMaxWidth(0.7f).height(2.5.dp).clip(RoundedCornerShape(2.dp)).background(purple))
            Box(Modifier.fillMaxWidth().height(2.5.dp).clip(RoundedCornerShape(2.dp)).background(purple))
        }
    }
}

@Composable
private fun SettingsCard(onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Card)
            .border(1.dp, Border, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        RingGlyph(size = 28.dp, color = IdentiaTheme.colors.primary, strokeWidth = 2.dp)
        Text("Settings", style = IdentiaTheme.type.bodySemi.copy(fontSize = 13.sp), color = IdentiaTheme.colors.textPrimary)
    }
}
