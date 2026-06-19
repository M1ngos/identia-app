package com.identia.app.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/** Extended color tokens not covered by Material's color scheme. */
@Immutable
data class IdentiaColors(
    val bg: Color = Bg,
    val bgAlt: Color = BgAlt,
    val card: Color = Card,
    val cardAlt: Color = CardAlt,
    val cardInput: Color = CardInput,
    val primary: Color = Primary,
    val primaryDeep: Color = PrimaryDeep,
    val accentSoft: Color = AccentSoft,
    val success: Color = Success,
    val error: Color = Error,
    val errorSoft: Color = ErrorSoft,
    val warning: Color = Warning,
    val textPrimary: Color = TextPrimary,
    val textDim: Color = TextDim,
    val textSecondary: Color = TextSecondary,
    val textSecondaryAlt: Color = TextSecondaryAlt,
    val textMuted: Color = TextMuted,
    val textMutedAlt: Color = TextMutedAlt,
    val textInactive: Color = TextInactive,
    val border: Color = Border,
    val borderSoft: Color = BorderSoft,
    val borderStrong: Color = BorderStrong,
)

private val LocalIdentiaColors = staticCompositionLocalOf { IdentiaColors() }
private val LocalIdentiaTypography = staticCompositionLocalOf { IdentiaTypography() }

private val IdentiaColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = PrimaryDeep,
    onPrimaryContainer = AccentSoft,
    secondary = AccentSoft,
    onSecondary = Color.White,
    background = Bg,
    onBackground = TextPrimary,
    surface = Card,
    onSurface = TextPrimary,
    surfaceVariant = CardAlt,
    onSurfaceVariant = TextSecondaryAlt,
    error = Error,
    onError = Color.White,
    outline = Border,
    outlineVariant = BorderSoft,
)

private val IdentiaMaterialTypography = Typography().run {
    val t = IdentiaTypography()
    copy(
        headlineLarge = t.headingLg,
        headlineMedium = t.headingMd,
        titleLarge = t.title,
        titleMedium = t.titleSm,
        bodyLarge = t.bodyStrong,
        bodyMedium = t.body,
        labelLarge = t.button,
        labelSmall = t.monoLabel,
    )
}

/** Accessor for IdentIA design tokens, MaterialTheme-style. */
object IdentiaTheme {
    val colors: IdentiaColors
        @Composable @ReadOnlyComposable get() = LocalIdentiaColors.current
    val type: IdentiaTypography
        @Composable @ReadOnlyComposable get() = LocalIdentiaTypography.current
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = IdentiaColorScheme,
        typography = IdentiaMaterialTypography,
        content = content,
    )
}
