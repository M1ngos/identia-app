package com.identia.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.identia.app.core.theme.Bg
import com.identia.app.core.theme.Border
import com.identia.app.core.theme.IdentiaTheme

/**
 * Full-screen container: paints the background, applies the top safe-area inset
 * (the fake "9:41" status bar from the mock is dropped in favour of real bars),
 * and lays out content in a Column.
 */
@Composable
fun ScreenScaffold(
    modifier: Modifier = Modifier,
    background: Color = Bg,
    brush: Brush? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val base = if (brush != null) Modifier.background(brush) else Modifier.background(background)
    Column(
        modifier = base
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
            .then(modifier),
        content = content,
    )
}

/** Header row with a back chevron in a rounded square and a Space-Grotesk title. */
@Composable
fun TopBarWithBack(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    titleColor: Color = IdentiaTheme.colors.textPrimary,
) {
    Row(
        modifier = modifier.padding(start = 18.dp, end = 18.dp, top = 8.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(13.dp),
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .border(1.dp, Border, RoundedCornerShape(10.dp))
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center,
        ) {
            ChevronLeftIcon(size = 14.dp, color = IdentiaTheme.colors.textSecondary)
        }
        Text(title, style = IdentiaTheme.type.titleSm, color = titleColor)
    }
}

/** Uppercase, tracked, mono section label (e.g. "MODULES", "RECENT CHECKS"). */
@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        style = IdentiaTheme.type.monoSm,
        color = IdentiaTheme.colors.textMuted,
        modifier = modifier,
    )
}

/** Centered helper text used over camera viewfinders. */
@Composable
fun ViewfinderHint(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = IdentiaTheme.type.mono,
        color = IdentiaTheme.colors.textSecondary,
        textAlign = TextAlign.Center,
        modifier = modifier,
    )
}
