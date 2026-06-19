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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.identia.app.core.theme.Bg
import com.identia.app.core.theme.Border
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
        Text(tab.label, style = IdentiaTheme.type.navLabel, color = color)
    }
}



@Composable
private fun NavGlyph(tab: Tab, color: Color) {
    when (tab) {
        Tab.Home -> Box(Modifier.size(16.dp).border(2.dp, color, RoundedCornerShape(5.dp)))
        Tab.Verify -> Box(Modifier.size(13.dp).rotate(45f).border(2.dp, color, RoundedCornerShape(3.dp)))
        Tab.Face -> Box(Modifier.size(15.dp).border(2.dp, color, CircleShape))
        Tab.Logs -> Column(
            Modifier.width(15.dp),
            verticalArrangement = Arrangement.spacedBy(2.5.dp),
        ) {
            repeat(3) { Box(Modifier.fillMaxWidth().height(2.dp).clip(RoundedCornerShape(2.dp)).background(color)) }
        }
    }
}
