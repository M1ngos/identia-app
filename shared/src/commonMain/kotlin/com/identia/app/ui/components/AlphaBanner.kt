package com.identia.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.identia.app.core.theme.IdentiaTheme

/**
 * Amber "ALPHA" pill ribbon — used prominently under the wordmark on the login screen.
 */
@Composable
fun AlphaPill(
    modifier: Modifier = Modifier,
    text: String = "ALPHA · PREVIEW BUILD — NOT FOR PRODUCTION",
) {
    val amber = IdentiaTheme.colors.warning
    val shape = RoundedCornerShape(99.dp)
    Row(
        modifier = modifier
            .clip(shape)
            .background(amber.copy(alpha = 0.10f))
            .border(1.dp, amber.copy(alpha = 0.35f), shape)
            .padding(horizontal = 13.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(Modifier.size(7.dp).clip(CircleShape).background(amber).pulse(0.3f, 1f, 1200))
        Text(text, style = IdentiaTheme.type.monoLabel, color = amber)
    }
}

/**
 * Slim persistent strip rendered above the authenticated content (bottom-nav screens).
 * Amber hairline-bordered band, mono uppercase.
 */
@Composable
fun AlphaStrip(
    modifier: Modifier = Modifier,
    text: String = "ALPHA — PREVIEW BUILD, NOT FOR PRODUCTION",
) {
    val amber = IdentiaTheme.colors.warning
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(amber.copy(alpha = 0.07f))
            .border(
                width = 1.dp,
                color = amber.copy(alpha = 0.22f),
                shape = RoundedCornerShape(0.dp),
            )
            .padding(vertical = 5.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp),
        ) {
            Box(Modifier.size(6.dp).clip(CircleShape).background(amber).pulse(0.3f, 1f, 1200))
            Text(text, style = IdentiaTheme.type.monoSm, color = amber)
        }
    }
}
