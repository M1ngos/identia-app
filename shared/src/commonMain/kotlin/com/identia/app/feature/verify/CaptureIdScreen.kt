package com.identia.app.feature.verify

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.identia.app.core.theme.BgPure
import com.identia.app.core.theme.IdentiaTheme
import com.identia.app.ui.components.Chip
import com.identia.app.ui.components.SegmentedProgress
import com.identia.app.ui.components.ShutterButton
import com.identia.app.ui.components.TopBarWithBack
import com.identia.app.ui.components.ViewfinderFrame
import com.identia.app.ui.components.ViewfinderHint
import com.identia.app.ui.components.ScreenScaffold

@Composable
fun CaptureIdScreen(front: Boolean, onBack: () -> Unit, onCapture: () -> Unit) {
    ScreenScaffold(background = BgPure) {
        TopBarWithBack(if (front) "Scan ID — Front" else "Scan ID — Back", onBack, titleColor = Color.White)
        SegmentedProgress(
            total = 4, active = 1,
            modifier = Modifier.padding(horizontal = 22.dp).padding(bottom = 12.dp),
        )
        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 4.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFF0E0E12)),
        ) {
            ViewfinderFrame(accent = IdentiaTheme.colors.primary)
            ViewfinderHint(
                "Align front of ID within frame",
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(bottom = 18.dp),
            )
        }
        Row(
            Modifier.fillMaxWidth().padding(top = 14.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        ) {
            Chip("Front", selected = front, onClick = {})
            Chip("Back", selected = !front, onClick = {})
        }
        Box(Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 24.dp), contentAlignment = Alignment.Center) {
            ShutterButton(onClick = onCapture)
        }
    }
}
