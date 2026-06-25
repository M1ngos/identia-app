package com.identia.app.feature.verify

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.identia.app.camera.isCameraAvailable
import com.identia.app.core.i18n.LocalStrings
import com.identia.app.ml.ExtractedField
import com.identia.app.ml.IdCardDetector
import com.identia.app.ml.decodeImageToArgb
import com.identia.app.ml.extractFields
import com.identia.app.ml.readFileBytes
import com.identia.app.core.theme.BgPure
import com.identia.app.core.theme.IdentiaTheme
import com.identia.app.ui.components.Chip
import com.identia.app.ui.components.SegmentedProgress
import com.identia.app.ui.components.ShutterButton
import com.identia.app.ui.components.TopBarWithBack
import com.identia.app.ui.components.ViewfinderFrame
import com.identia.app.ui.components.ViewfinderHint
import com.identia.app.ui.components.ScreenScaffold
import com.kashif.cameraK.compose.CameraPreviewView
import com.kashif.cameraK.compose.rememberCameraKState
import com.kashif.cameraK.enums.AspectRatio
import com.kashif.cameraK.enums.CameraLens
import com.kashif.cameraK.enums.Directory
import com.kashif.cameraK.enums.FlashMode
import com.kashif.cameraK.enums.ImageFormat
import com.kashif.cameraK.result.ImageCaptureResult
import com.kashif.cameraK.state.CameraConfiguration
import com.kashif.cameraK.state.CameraKState
import kotlinx.coroutines.launch

@Composable
fun CaptureIdScreen(
    front: Boolean,
    onBack: () -> Unit,
    onCapture: () -> Unit,
    onExtracted: (List<ExtractedField>) -> Unit = {},
) {
    val strings = LocalStrings.current
    val scope = rememberCoroutineScope()
    var capturing by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf<String?>(null) }

    // Skip camera init entirely when no camera is present (e.g. desktop with no
    // webcam). On desktop CameraK only logs a grabber failure and never reports
    // an error state, so without this the preview would stay blank forever.
    val cameraAvailable = remember { isCameraAvailable() }

    // Load the YOLO ID detector once. Loading is a no-op where ONNX is unavailable
    // (iOS session is stubbed); detection is then skipped and capture still advances.
    var detector by remember { mutableStateOf<IdCardDetector?>(null) }
    if (cameraAvailable) {
        LaunchedEffect(Unit) {
            detector = runCatching { IdCardDetector.load() }.getOrNull()
        }
        DisposableEffect(Unit) {
            onDispose { detector?.close() }
        }
    }

    // Live back-camera feed; CameraK requests the camera permission during init.
    val cameraStateHolder: State<CameraKState>? = if (cameraAvailable) {
        rememberCameraKState(
            config = CameraConfiguration(
                cameraLens = CameraLens.BACK,
                flashMode = FlashMode.OFF,
                imageFormat = ImageFormat.JPEG,
                aspectRatio = AspectRatio.RATIO_4_3,
                directory = Directory.DOCUMENTS,
            ),
        )
    } else {
        null
    }
    val cameraState = cameraStateHolder?.value

    // Capture the frame, run ID detection, then advance. We only block progress
    // when detection actually ran and didn't find the expected card side; if the
    // camera or detector is unavailable, the demo flow keeps moving.
    val expectedClass = if (front) "front_card" else "back_card"

    fun shutter() {
        if (capturing) return
        val state = cameraState
        if (state !is CameraKState.Ready) {
            onCapture()
            return
        }
        capturing = true
        statusMessage = null
        scope.launch {
            // null = detection couldn't run; a list = it ran (possibly empty).
            // OCR of the detected fields is captured into `fields` as a side effect.
            var fields: List<ExtractedField> = emptyList()
            val detections = runCatching {
                val capture = state.controller.takePictureToFile()
                val bytes = when (capture) {
                    is ImageCaptureResult.Success -> capture.byteArray
                    is ImageCaptureResult.SuccessWithFile -> readFileBytes(capture.filePath)
                    is ImageCaptureResult.Error -> null
                }
                val det = detector
                val image = bytes?.let { decodeImageToArgb(it) }
                if (det != null && image != null) {
                    val found = det.detect(image.pixels, image.width, image.height)
                    // Read the text inside each detected field box (no-op where OCR
                    // is unavailable). Failures here never block capture.
                    fields = runCatching { extractFields(image, found) }.getOrDefault(emptyList())
                    found
                } else {
                    null
                }
            }.getOrNull()
            capturing = false
            when {
                detections == null -> onCapture() // detector/decoder unavailable — don't block
                detections.any { it.label == expectedClass } -> {
                    onExtracted(fields)
                    onCapture()
                }
                else -> statusMessage = strings.idNotDetected
            }
        }
    }

    ScreenScaffold(background = BgPure) {
        TopBarWithBack(if (front) strings.scanIdFront else strings.scanIdBack, onBack, titleColor = Color.White)
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
            when (val state = cameraState) {
                null ->
                    Text(
                        strings.noCameraDetected,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center).padding(24.dp),
                    )
                is CameraKState.Ready ->
                    CameraPreviewView(controller = state.controller, modifier = Modifier.fillMaxSize())
                is CameraKState.Error ->
                    Text(
                        state.message,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center).padding(24.dp),
                    )
                CameraKState.Initializing ->
                    CircularProgressIndicator(
                        color = IdentiaTheme.colors.primary,
                        modifier = Modifier.align(Alignment.Center),
                    )
            }
            ViewfinderFrame(accent = IdentiaTheme.colors.primary)
            if (capturing) {
                CircularProgressIndicator(
                    color = IdentiaTheme.colors.primary,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
            ViewfinderHint(
                statusMessage ?: strings.alignFrontOfId,
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(bottom = 18.dp),
            )
        }
        Row(
            Modifier.fillMaxWidth().padding(top = 14.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        ) {
            Chip(strings.front, selected = front, onClick = {})
            Chip(strings.back, selected = !front, onClick = {})
        }
        Box(Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 24.dp), contentAlignment = Alignment.Center) {
            ShutterButton(onClick = ::shutter)
        }
    }
}
