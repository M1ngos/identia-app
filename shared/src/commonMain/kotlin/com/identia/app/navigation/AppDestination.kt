package com.identia.app.navigation

import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes for Jetpack/Compose-Multiplatform Navigation.
 * Primitives only (Navigation type-safe args) — enums modelled as Boolean/Float.
 */

@Serializable object AuthRoute

// Bottom-nav tab pages (show the bottom bar)
@Serializable object HomeRoute
@Serializable object LogsRoute

// Reached from Home grid (full screen, back chevron)
@Serializable object SettingsRoute

// Identity-verification sub-flow
@Serializable object VerifyStartRoute
@Serializable data class CaptureRoute(val front: Boolean = true)
@Serializable object SelfieRoute
@Serializable object LivenessRoute
@Serializable data class VerifyResultRoute(val failed: Boolean)

// Face-authentication sub-flow
@Serializable object FaceCameraRoute
@Serializable object FaceScanningRoute
@Serializable data class FaceMatchRoute(val confidence: Float)

/** Bottom-navigation tabs. Verify & Face are entry points into their sub-flows. */
enum class Tab(val label: String) {
    Home("Home"),
    Verify("Verify"),
    Face("Face"),
    Logs("Logs"),
}
