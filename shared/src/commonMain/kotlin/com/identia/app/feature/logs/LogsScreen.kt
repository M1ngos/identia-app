package com.identia.app.feature.logs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.identia.app.core.theme.Bg
import com.identia.app.core.theme.Card
import com.identia.app.core.theme.IdentiaTheme
import com.identia.app.core.theme.Success
import com.identia.app.state.AuditEntry
import com.identia.app.state.LocalDemoState
import com.identia.app.ui.components.Chip
import com.identia.app.ui.components.ScreenScaffold

private enum class Filter { All, Success, Failed, Date }

@Composable
fun LogsScreen() {
    val demo = LocalDemoState.current
    var filter by remember { mutableStateOf(Filter.All) }

    fun keep(e: AuditEntry) = when (filter) {
        Filter.Success -> e.status == AuditEntry.Status.Ok
        Filter.Failed -> e.status == AuditEntry.Status.Fail
        else -> true
    }
    val today = demo.auditToday.filter(::keep)
    val jun10 = demo.auditJun10.filter(::keep)

    ScreenScaffold {
        Text(
            "Audit Log",
            style = IdentiaTheme.type.title,
            color = IdentiaTheme.colors.textPrimary,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 10.dp),
        )
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(7.dp),
        ) {
            Chip("All", filter == Filter.All) { filter = Filter.All }
            Chip("Success", filter == Filter.Success) { filter = Filter.Success }
            Chip("Failed", filter == Filter.Failed) { filter = Filter.Failed }
            Chip("Date", filter == Filter.Date) { filter = Filter.Date }
        }
        Box(Modifier.fillMaxWidth().padding(horizontal = 20.dp).verticalScroll(rememberScrollState())) {
            // Timeline rail
            Box(Modifier.padding(start = 9.dp).width(2.dp).fillMaxHeight().background(IdentiaTheme.colors.border))
            Column(Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                if (today.isNotEmpty()) {
                    DayHeader("Today")
                    today.forEach { TimelineEntry(it) }
                }
                if (jun10.isNotEmpty()) {
                    Spacer(Modifier.height(2.dp))
                    DayHeader("Jun 10")
                    jun10.forEach { TimelineEntry(it) }
                }
            }
        }
    }
}

@Composable
private fun DayHeader(text: String) {
    Text(
        text.uppercase(),
        style = IdentiaTheme.type.monoSm.copy(letterSpacing = androidx.compose.ui.unit.TextUnit.Unspecified),
        color = IdentiaTheme.colors.textMuted,
        modifier = Modifier.padding(start = 22.dp, top = 6.dp, bottom = 13.dp),
    )
}

@Composable
private fun TimelineEntry(entry: AuditEntry) {
    val ok = entry.status == AuditEntry.Status.Ok
    val color = if (ok) Success else IdentiaTheme.colors.error
    Row(
        Modifier.fillMaxWidth().padding(bottom = 13.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Node
        Box(
            Modifier.size(18.dp).clip(CircleShape).background(Bg).border(2.dp, color, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Box(Modifier.size(6.dp).clip(CircleShape).background(color))
        }
        // Card
        Column(
            Modifier
                .weight(1f)
                .clip(RoundedCornerShape(13.dp))
                .background(Card)
                .border(1.dp, if (ok) IdentiaTheme.colors.border else IdentiaTheme.colors.error.copy(alpha = 0.2f), RoundedCornerShape(13.dp))
                .padding(horizontal = 13.dp, vertical = 11.dp),
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(entry.title, style = IdentiaTheme.type.bodyStrong, color = IdentiaTheme.colors.textPrimary)
                Text(entry.time, style = IdentiaTheme.type.monoSm.copy(letterSpacing = androidx.compose.ui.unit.TextUnit.Unspecified), color = IdentiaTheme.colors.textSecondaryAlt)
            }
            Spacer(Modifier.height(3.dp))
            Text(
                entry.detail,
                style = IdentiaTheme.type.mono,
                color = if (ok) IdentiaTheme.colors.textSecondaryAlt else IdentiaTheme.colors.errorSoft,
            )
        }
    }
}
