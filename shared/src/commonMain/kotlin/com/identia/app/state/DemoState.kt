package com.identia.app.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import com.identia.app.core.i18n.Language
import com.identia.app.ml.ExtractedField

/** A single audit-log entry. */
data class AuditEntry(
    val title: String,
    val detail: String,
    val time: String,
    val status: Status,
) {
    enum class Status { Ok, Fail }
}

/**
 * Lightweight in-memory demo state. Holds the sample data shown across screens
 * plus the toggles surfaced on Settings (the "Simulate failure" switch drives
 * which verification result screen the Liveness step navigates to).
 */
class DemoState {
    val userName = "eau7829"
    val userEmail = "early-adopter@proton.me"
    val trustScore = 98

    /** Whether the user has cleared the 6-digit code gate. */
    var isAuthenticated by mutableStateOf(false)

    var darkMode by mutableStateOf(true)
    var biometricUnlock by mutableStateOf(true)
    var simulateFailure by mutableStateOf(false)

    /** Selected interface language; drives the strings provided via LocalStrings. */
    var language by mutableStateOf(Language.English)

    /**
     * Text fields read off the ID during capture (OCR over the YOLO-detected
     * boxes). Accumulated across the front and back captures; the latest read of a
     * given field wins. Empty where OCR is unavailable (iOS/desktop).
     */
    var idFields by mutableStateOf<List<ExtractedField>>(emptyList())
        private set

    /** Merge a capture's OCR results in, replacing any prior value for the same field. */
    fun recordIdFields(fields: List<ExtractedField>) {
        if (fields.isEmpty()) return
        val byLabel = LinkedHashMap<String, ExtractedField>()
        idFields.forEach { byLabel[it.label] = it }
        fields.forEach { byLabel[it.label] = it }
        idFields = byLabel.values.toList()
    }

    /** Clear the session — called from the Log Out action. */
    fun logOut() {
        isAuthenticated = false
    }

    companion object {
        const val IDENTIA_APP_VERSION: String = "v0.1.7-alpha"

        /** Demo-only access code. Alpha build: the only way in. */
        const val DEMO_ACCESS_CODE = "123456"
    }

    val auditToday = listOf(
        AuditEntry("Face auth · ok", "conf 99.2% · 412ms", "09:41", AuditEntry.Status.Ok),
        AuditEntry("Login · ok", "device · iPhone 15", "08:12", AuditEntry.Status.Ok),
    )
    val auditJun10 = listOf(
        AuditEntry("Face auth · fail", "conf 61% · below threshold", "21:30", AuditEntry.Status.Fail),
        AuditEntry("ID verified · ok", "passport ····7421", "14:02", AuditEntry.Status.Ok),
    )
}

val LocalDemoState = staticCompositionLocalOf<DemoState> {
    error("DemoState not provided")
}

@Composable
fun rememberDemoState(): DemoState = remember { DemoState() }
