package com.identia.app.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

/** A single audit-log entry. */
data class AuditEntry(
    val title: String,
    val detail: String,
    val time: String,
    val status: Status,
) {
    enum class Status { Ok, Fail }
}

/** A recent check shown on the Profile screen. */
data class RecentCheck(
    val title: String,
    val subtitle: String,
    val ok: Boolean,
)

/**
 * Lightweight in-memory demo state. Holds the sample data shown across screens
 * plus the toggles surfaced on Settings (the "Simulate failure" switch drives
 * which verification result screen the Liveness step navigates to).
 */
class DemoState {
    val userName = "Alex Mercer"
    val userEmail = "alex.mercer@identia.io"
    val initials = "AM"
    val trustScore = 98

    /** Whether the user has cleared the 6-digit code gate. */
    var isAuthenticated by mutableStateOf(false)

    var darkMode by mutableStateOf(true)
    var biometricUnlock by mutableStateOf(true)
    var simulateFailure by mutableStateOf(false)

    /** Clear the session — called from the Log Out action. */
    fun logOut() {
        isAuthenticated = false
    }

    companion object {
        /** Demo-only access code. Alpha build: the only way in. */
        const val DEMO_ACCESS_CODE = "123456"
    }

    val recentChecks = listOf(
        RecentCheck("Face Authentication", "Today · 99.2%", ok = true),
        RecentCheck("Identity Verification", "Jun 12 · passed", ok = true),
        RecentCheck("Face Authentication", "Jun 10 · 61% (failed)", ok = false),
    )

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
