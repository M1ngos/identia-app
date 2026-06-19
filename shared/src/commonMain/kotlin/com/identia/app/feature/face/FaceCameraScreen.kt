package com.identia.app.feature.face

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.identia.app.core.theme.BgPure
import com.identia.app.core.theme.IdentiaTheme
import com.identia.app.ui.components.CornerBrackets
import com.identia.app.ui.components.FaceOvalGuide
import com.identia.app.ui.components.PrimaryButton
import com.identia.app.ui.components.ScreenScaffold
import com.identia.app.ui.components.TopBarWithBack
import com.identia.app.ui.components.ViewfinderHint

@Composable
fun FaceCameraScreen(onBack: () -> Unit, onScan: () -> Unit) {
    ScreenScaffold(background = BgPure) {
        TopBarWithBack("Face Authentication", onBack, titleColor = Color.White)
        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Brush.radialGradient(listOf(Color(0xFF161320), BgPure))),
            contentAlignment = Alignment.Center,
        ) {
            CornerBrackets(color = Color.White.copy(alpha = 0.35f))
            FaceOvalGuide(width = 170.dp, height = 210.dp, color = IdentiaTheme.colors.primary.copy(alpha = 0.5f))
            ViewfinderHint(
                "Position your face to begin",
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(bottom = 20.dp),
            )
        }
        PrimaryButton(
            "Scan Face",
            onClick = onScan,
            modifier = Modifier.padding(horizontal = 24.dp).padding(top = 18.dp, bottom = 26.dp),
        ) {
            Box(Modifier.size(16.dp).clip(CircleShape).border(2.dp, Color.White, CircleShape))
        }
    }
}
