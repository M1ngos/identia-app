package com.identia.app.camera

/**
 * Android always exposes the camera APIs; CameraK reports permission denial or
 * hardware failure through [com.kashif.cameraK.state.CameraKState.Error], which
 * the UI handles. (Camera-less Android devices are vanishingly rare.)
 */
actual fun isCameraAvailable(): Boolean = true
