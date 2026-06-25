package com.identia.app.camera

/**
 * Whether this device/host has a usable camera.
 *
 * Used to decide up front whether to start the camera at all. On desktop, where
 * CameraK only logs a grabber failure (it never surfaces a [com.kashif.cameraK.state.CameraKState.Error]),
 * this lets us show a "no camera" message instead of a permanently blank preview.
 */
expect fun isCameraAvailable(): Boolean
