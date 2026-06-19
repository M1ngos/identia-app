package com.identia.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.identia.app.core.theme.BorderStrong
import com.identia.app.core.theme.Gradients
import com.identia.app.core.theme.IdentiaTheme

/** Primary purple gradient button with glow. */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: (@Composable () -> Unit)? = null,
) {
    val shape = RoundedCornerShape(14.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .glow(IdentiaTheme.colors.primary.copy(alpha = 0.7f), 18.dp, shape)
            .background(Gradients.purple, shape)
            .clickable(onClick = onClick)
            .padding(vertical = 15.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content?.invoke()
        Text(text, style = IdentiaTheme.type.button, color = Color.White)
    }
}

/** Outlined transparent secondary button. */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(14.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, BorderStrong, shape)
            .clickable(onClick = onClick)
            .padding(vertical = 15.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text,
            style = IdentiaTheme.type.button.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Medium),
            color = IdentiaTheme.colors.textPrimary,
            textAlign = TextAlign.Center,
        )
    }
}

/** Dashed purple "ENTER DEMO MODE" affordance. */
@Composable
fun DashedDemoButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(14.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(IdentiaTheme.colors.primary.copy(alpha = 0.08f), shape)
            .dashedBorder(IdentiaTheme.colors.primary.copy(alpha = 0.5f), 1.dp, 14.dp)
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text,
            style = IdentiaTheme.type.monoLabel.copy(fontSize = 13.sp, letterSpacing = 0.06.em),
            color = IdentiaTheme.colors.accentSoft,
        )
    }
}

/** Danger / destructive outlined button (Reset Demo Data). */
@Composable
fun DangerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leading: (@Composable () -> Unit)? = null,
) {
    val shape = RoundedCornerShape(14.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(IdentiaTheme.colors.error.copy(alpha = 0.10f), shape)
            .border(1.dp, IdentiaTheme.colors.error.copy(alpha = 0.3f), shape)
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leading?.invoke()
        Text(text, style = IdentiaTheme.type.bodySemi, color = IdentiaTheme.colors.errorSoft)
    }
}
