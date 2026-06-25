package com.identia.app.camera

/**
 * iOS devices have a camera; CameraK reports permission denial through
 * [com.kashif.cameraK.state.CameraKState.Error], which the UI handles.
 */
actual fun isCameraAvailable(): Boolean = true
