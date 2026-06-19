package com.identia.app.core.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/** Linear brushes pulled from the design mock. */
object Gradients {
    /** Primary purple button / logo gradient (≈150deg). */
    val purple = Brush.verticalGradient(listOf(Primary, PrimaryDeep))

    /** Avatar / soft surface gradient. */
    val avatar = Brush.verticalGradient(listOf(Color(0xFF2A2A36), Color(0xFF15151C)))

    /** Home status banner: greenish → purple wash. */
    val successBanner = Brush.linearGradient(
        listOf(Success.copy(alpha = 0.14f), Primary.copy(alpha = 0.08f)),
    )

    /** Phone bezel gradient (desktop preview frame). */
    val bezel = Brush.verticalGradient(listOf(Color(0xFF27272F), Color(0xFF0C0C10)))
}
