package com.identia.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.identia.app.core.theme.Bg
import com.identia.app.core.theme.Border
import com.identia.app.core.i18n.LocalStrings
import com.identia.app.core.theme.IdentiaTheme
import com.identia.app.navigation.Tab

@Composable
fun BottomNav(selected: Tab?, onSelect: (Tab) -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Bg)
            .border(1.dp, Border, RoundedCornerShape(0.dp)),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp)
                .padding(top = 11.dp, bottom = 8.dp)
                .windowInsetsPadding(WindowInsets.navigationBars),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Top,
        ) {
            Tab.entries.forEach { tab ->
                NavItem(tab = tab, active = tab == selected, onClick = { onSelect(tab) })
            }
        }
    }
}

@Composable
private fun NavItem(tab: Tab, active: Boolean, onClick: () -> Unit) {
    val strings = LocalStrings.current
    val label = when (tab) {
        Tab.Home -> strings.tabHome
        Tab.Verify -> strings.tabVerify
        Tab.Face -> strings.tabFace
        Tab.Logs -> strings.tabLogs
    }
    val color = if (active) IdentiaTheme.colors.primary else IdentiaTheme.colors.textInactive
    Column(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Box(Modifier.size(18.dp), contentAlignment = Alignment.Center) {
            NavGlyph(tab, color)
        }
        Text(label, style = IdentiaTheme.type.navLabel, color = color)
    }
}



@Composable
private fun NavGlyph(tab: Tab, color: Color) {
    val vector = when (tab) {
        Tab.Home -> Icons.Outlined.Home
        Tab.Verify -> Icons.Outlined.Badge
        Tab.Face -> Icons.Outlined.Face
        Tab.Logs -> Icons.Outlined.Description
    }
    Icon(vector, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
}
