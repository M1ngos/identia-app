package com.identia.app.feature.verify

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.identia.app.core.theme.BgPure
import com.identia.app.core.theme.IdentiaTheme
import com.identia.app.core.theme.Success
import com.identia.app.ui.components.FaceOvalGuide
import com.identia.app.ui.components.ScreenScaffold
import com.identia.app.ui.components.SegmentedProgress
import com.identia.app.ui.components.ShutterButton
import com.identia.app.ui.components.TopBarWithBack
import com.identia.app.ui.components.ViewfinderHint

@Composable
fun SelfieScreen(onBack: () -> Unit, onCapture: () -> Unit) {
    ScreenScaffold(background = BgPure) {
        TopBarWithBack("Take a Selfie", onBack, titleColor = Color.White)
        SegmentedProgress(
            total = 4, active = 2,
            modifier = Modifier.padding(horizontal = 22.dp).padding(bottom = 12.dp),
        )
        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 4.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Brush.radialGradient(listOf(Color(0xFF161320), BgPure))),
            contentAlignment = Alignment.Center,
        ) {
            FaceOvalGuide(width = 170.dp, height = 215.dp, color = IdentiaTheme.colors.primary.copy(alpha = 0.55f))
            ViewfinderHint(
                "Center your face in the oval",
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(bottom = 18.dp),
            )
        }
        Row(
            Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(Modifier.size(7.dp).clip(CircleShape).background(Success))
            Text("Good lighting detected", style = IdentiaTheme.type.mono, color = Success)
        }
        Box(Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 24.dp), contentAlignment = Alignment.Center) {
            ShutterButton(onClick = onCapture)
        }
    }
}
