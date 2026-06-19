package com.identia.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.identia.app.core.theme.Border
import com.identia.app.core.theme.Card
import com.identia.app.core.theme.CardAlt
import com.identia.app.core.theme.IdentiaTheme

/** Standard #15151c card with hairline border. */
@Composable
fun IdentiaCard(
    modifier: Modifier = Modifier,
    radius: Int = 16,
    background: Color = Card,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(radius.dp))
            .background(background)
            .border(1.dp, Border, RoundedCornerShape(radius.dp)),
        content = content,
    )
}

/** Divided card (#13131a) holding label/value or issue rows. */
@Composable
fun DividedCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) = IdentiaCard(modifier = modifier, background = CardAlt, content = content)

@Composable
fun RowDivider() = HorizontalDivider(thickness = 1.dp, color = IdentiaTheme.colors.borderSoft)

@Composable
fun DetailRow(label: String, value: String, valueColor: Color = IdentiaTheme.colors.textPrimary) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 13.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, style = IdentiaTheme.type.body, color = IdentiaTheme.colors.textSecondaryAlt)
        Text(value, style = IdentiaTheme.type.bodyStrong, color = valueColor)
    }
}

@Composable
fun IssueRow(text: String, dotColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 13.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(8.dp).clip(CircleShape).background(dotColor))
        Text(text, style = IdentiaTheme.type.body, color = IdentiaTheme.colors.textPrimary)
    }
}

/** Selectable pill chip (filter tabs, front/back). */
@Composable
fun Chip(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(99.dp)
    val base = if (selected) {
        Modifier.background(IdentiaTheme.colors.primary, shape)
    } else {
        Modifier.background(Card, shape).border(1.dp, IdentiaTheme.colors.border, shape)
    }
    Box(
        modifier = modifier.then(base).clickable(onClick = onClick).padding(horizontal = 14.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text,
            style = IdentiaTheme.type.monoLabel.copy(letterSpacing = androidx.compose.ui.unit.TextUnit.Unspecified),
            color = if (selected) Color.White else IdentiaTheme.colors.textSecondary,
        )
    }
}

/** Static info pill (e.g. "≈ 60 seconds · secure"). */
@Composable
fun InfoPill(text: String, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(99.dp)
    Box(
        modifier = modifier.background(Card, shape).border(1.dp, Border, shape)
            .padding(horizontal = 12.dp, vertical = 7.dp),
    ) {
        Text(text, style = IdentiaTheme.type.mono, color = IdentiaTheme.colors.textSecondaryAlt)
    }
}
