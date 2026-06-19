package com.identia.app.core.theme

import androidx.compose.ui.graphics.Color

/**
 * IdentIA design tokens — dark, security-grade palette.
 * Source of truth: docs/identia-handoff/project/IdentIA.dc.html
 */

// Backgrounds
val Bg = Color(0xFF0B0B0F)
val BgAlt = Color(0xFF0A0A0C)
val BgPure = Color(0xFF000000)

// Surfaces / cards
val Card = Color(0xFF15151C)
val CardAlt = Color(0xFF13131A)
val CardInput = Color(0xFF0D0D12)

// Brand purple
val Primary = Color(0xFF7C5CFF)
val PrimaryDeep = Color(0xFF4A2FD6)
val PrimaryDeepAlt = Color(0xFF5B34D6)
val AccentSoft = Color(0xFFB9A8FF)

// Semantic
val Success = Color(0xFF34E5A3)
val Error = Color(0xFFFF5C7A)
val ErrorSoft = Color(0xFFFF8DA1)
val Warning = Color(0xFFFFC15C)

// Text
val TextPrimary = Color(0xFFF4F4F6)
val TextDim = Color(0xFFE6E6EA)
val TextSecondary = Color(0xFF9A9AA6)
val TextSecondaryAlt = Color(0xFF84848F)
val TextMuted = Color(0xFF6B6B78)
val TextMutedAlt = Color(0xFF55555F)
val TextInactive = Color(0xFF5D5D68)

// Hairline borders (white at low alpha)
val Border = Color(0x12FFFFFF)       // ~7%
val BorderSoft = Color(0x0FFFFFFF)   // ~6%
val BorderStrong = Color(0x24FFFFFF) // ~14%
