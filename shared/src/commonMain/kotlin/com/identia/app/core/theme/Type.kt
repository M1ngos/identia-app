package com.identia.app.core.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

/**
 * Three font "roles" from the design, approximated with platform defaults:
 *  - display  → Space Grotesk   (SansSerif, bold/semibold, tight tracking)
 *  - body     → IBM Plex Sans   (SansSerif)
 *  - mono     → IBM Plex Mono   (Monospace, used for labels/timestamps/uppercase)
 */
private val Display = FontFamily.SansSerif
private val Body = FontFamily.SansSerif
private val Mono = FontFamily.Monospace

@Immutable
data class IdentiaTypography(
    // Display (Space Grotesk)
    val brand: TextStyle = TextStyle(fontFamily = Display, fontWeight = FontWeight.Bold, fontSize = 28.sp, letterSpacing = (-0.01).em),
    val headingLg: TextStyle = TextStyle(fontFamily = Display, fontWeight = FontWeight.Bold, fontSize = 24.sp, letterSpacing = (-0.01).em),
    val headingMd: TextStyle = TextStyle(fontFamily = Display, fontWeight = FontWeight.Bold, fontSize = 22.sp, letterSpacing = (-0.01).em),
    val title: TextStyle = TextStyle(fontFamily = Display, fontWeight = FontWeight.SemiBold, fontSize = 19.sp),
    val titleSm: TextStyle = TextStyle(fontFamily = Display, fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
    val numberHero: TextStyle = TextStyle(fontFamily = Display, fontWeight = FontWeight.Bold, fontSize = 44.sp),
    val numberLg: TextStyle = TextStyle(fontFamily = Display, fontWeight = FontWeight.Bold, fontSize = 34.sp),
    val statNumber: TextStyle = TextStyle(fontFamily = Display, fontWeight = FontWeight.Bold, fontSize = 20.sp),

    // Body (IBM Plex Sans)
    val body: TextStyle = TextStyle(fontFamily = Body, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 20.sp),
    val bodyStrong: TextStyle = TextStyle(fontFamily = Body, fontWeight = FontWeight.Medium, fontSize = 14.sp),
    val bodySemi: TextStyle = TextStyle(fontFamily = Body, fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
    val button: TextStyle = TextStyle(fontFamily = Body, fontWeight = FontWeight.SemiBold, fontSize = 15.sp),

    // Mono (IBM Plex Mono)
    val mono: TextStyle = TextStyle(fontFamily = Mono, fontWeight = FontWeight.Normal, fontSize = 11.sp),
    val monoSm: TextStyle = TextStyle(fontFamily = Mono, fontWeight = FontWeight.Normal, fontSize = 10.sp, letterSpacing = 0.14.em),
    val monoLabel: TextStyle = TextStyle(fontFamily = Mono, fontWeight = FontWeight.Medium, fontSize = 11.sp, letterSpacing = 0.14.em),
    val monoTag: TextStyle = TextStyle(fontFamily = Mono, fontWeight = FontWeight.Medium, fontSize = 12.sp, letterSpacing = 0.16.em),
    val navLabel: TextStyle = TextStyle(fontFamily = Mono, fontWeight = FontWeight.Normal, fontSize = 9.sp),
)
