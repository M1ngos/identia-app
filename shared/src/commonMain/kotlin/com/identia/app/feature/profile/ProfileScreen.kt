package com.identia.app.feature.profile

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
import com.identia.app.core.theme.Card
import com.identia.app.core.theme.Gradients
import com.identia.app.core.theme.IdentiaTheme
import com.identia.app.core.theme.Success
import com.identia.app.state.LocalDemoState
import com.identia.app.state.RecentCheck
import com.identia.app.ui.components.CheckIcon
import com.identia.app.ui.components.DangerButton
import com.identia.app.ui.components.ScreenScaffold
import com.identia.app.ui.components.SectionLabel
import com.identia.app.ui.components.StatCard
import com.identia.app.ui.components.glow

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    val demo = LocalDemoState.current
    ScreenScaffold {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 6.dp, bottom = 16.dp),
        ) {
            // Identity header
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier
                        .size(74.dp)
                        .glow(IdentiaTheme.colors.primary.copy(alpha = 0.3f), 20.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Gradients.avatar)
                        .border(1.dp, IdentiaTheme.colors.primary.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(demo.initials, style = IdentiaTheme.type.headingMd, color = IdentiaTheme.colors.accentSoft)
                }
                Spacer(Modifier.height(12.dp))
                Text(demo.userName, style = IdentiaTheme.type.title, color = IdentiaTheme.colors.textPrimary)
                Spacer(Modifier.height(3.dp))
                Text(demo.userEmail, style = IdentiaTheme.type.mono, color = IdentiaTheme.colors.textSecondaryAlt)
                Spacer(Modifier.height(12.dp))
                VerifiedBadge()
            }
            Spacer(Modifier.height(18.dp))
            // Stats
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                StatCard("24", "Checks", IdentiaTheme.colors.textPrimary, Modifier.weight(1f))
                StatCard("${demo.trustScore}", "Trust", Success, Modifier.weight(1f))
                StatCard("L2", "Tier", IdentiaTheme.colors.textPrimary, Modifier.weight(1f))
            }
            Spacer(Modifier.height(18.dp))
            SectionLabel("Recent Checks")
            Spacer(Modifier.height(10.dp))
            demo.recentChecks.forEachIndexed { index, check ->
                if (index > 0) Spacer(Modifier.height(9.dp))
                RecentCheckRow(check)
            }
            Spacer(Modifier.height(18.dp))
            DangerButton("Log Out", onClick = onLogout) {
                Box(Modifier.size(13.dp).clip(CircleShape).border(2.dp, IdentiaTheme.colors.errorSoft, CircleShape))
            }
        }
    }
}

@Composable
private fun VerifiedBadge() {
    Row(
        Modifier
            .clip(RoundedCornerShape(99.dp))
            .background(Success.copy(alpha = 0.12f))
            .border(1.dp, Success.copy(alpha = 0.3f), RoundedCornerShape(99.dp))
            .padding(horizontal = 13.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp),
    ) {
        CheckIcon(size = 12.dp, color = Success, strokeWidth = 2.dp)
        Text("Verified Identity", style = IdentiaTheme.type.bodyStrong, color = Success)
    }
}

@Composable
private fun RecentCheckRow(check: RecentCheck) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(13.dp))
            .background(Card)
            .border(1.dp, IdentiaTheme.colors.border, RoundedCornerShape(13.dp))
            .padding(horizontal = 13.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(11.dp),
    ) {
        Box(Modifier.size(9.dp).clip(CircleShape).background(if (check.ok) Success else IdentiaTheme.colors.error))
        Column(Modifier.weight(1f)) {
            Text(check.title, style = IdentiaTheme.type.bodyStrong, color = IdentiaTheme.colors.textPrimary)
            Text(check.subtitle, style = IdentiaTheme.type.mono, color = IdentiaTheme.colors.textSecondaryAlt)
        }
    }
}
