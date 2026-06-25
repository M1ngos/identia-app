package com.identia.app.camera

import java.io.File

/**
 * Desktop camera detection.
 *
 * On Linux, CameraK grabs from `/dev/video*`; if none exists it logs
 * "Cannot open video device /dev/video0" and the preview stays blank. We mirror
 * its device lookup here so we can detect that case before starting the camera.
 *
 * On Windows/macOS the device cannot be enumerated cheaply without opening a
 * grabber, so we optimistically assume a camera is present; a real open failure
 * still falls back to the demo flow at the shutter.
 */
actual fun isCameraAvailable(): Boolean {
    val os = System.getProperty("os.name").orEmpty().lowercase()
    return when {
        os.contains("linux") -> hasLinuxVideoDevice()
        else -> true
    }
}

private fun hasLinuxVideoDevice(): Boolean {
    val dev = File("/dev")
    val devices = dev.listFiles { file -> file.name.startsWith("video") && file.canRead() }
    return !devices.isNullOrEmpty()
}
