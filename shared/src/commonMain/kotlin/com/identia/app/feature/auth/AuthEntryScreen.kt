package com.identia.app.feature.auth

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.identia.app.core.theme.Bg
import com.identia.app.core.theme.CardInput
import com.identia.app.core.theme.IdentiaTheme
import com.identia.app.state.DemoState
import com.identia.app.state.LocalDemoState
import com.identia.app.ui.components.AlphaPill
import com.identia.app.ui.components.Logo
import com.identia.app.ui.components.PrimaryButton
import com.identia.app.ui.components.glow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val CODE_LENGTH = 6

/**
 * Code-based login: the only way into the app.
 *
 * A hidden [BasicTextField] (numeric keyboard, max 6 digits, supports paste/backspace)
 * drives 6 styled digit cells. Auto-submits on the 6th digit; shows loading then
 * either proceeds or flashes an error state (red cells + shake) and clears.
 */
@Composable
fun AuthEntryScreen(onEnter: () -> Unit) {
    val demo = LocalDemoState.current
    val scope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    var value by remember { mutableStateOf(TextFieldValue("")) }
    var isVerifying by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    // Horizontal shake offset used on error.
    val shake = remember { Animatable(0f) }

    fun verify() {
        if (isVerifying) return
        keyboard?.hide()
        isVerifying = true
        isError = false
        scope.launch {
            delay(600) // simulated server check
            if (value.text == DemoState.DEMO_ACCESS_CODE) {
                isVerifying = false
                demo.isAuthenticated = true
                onEnter()
            } else {
                isVerifying = false
                isError = true
                // Shake animation.
                val keyframes = listOf(0f, -14f, 12f, -9f, 6f, -3f, 0f)
                for (target in keyframes) {
                    shake.animateTo(target, tween(45))
                }
                value = TextFieldValue("")
                focusRequester.requestFocus()
                keyboard?.show()
            }
        }
    }

    fun onValueChange(new: TextFieldValue) {
        if (isVerifying) return
        val digits = new.text.filter { it.isDigit() }.take(CODE_LENGTH)
        // Reset error as soon as the user edits again.
        if (isError) isError = false
        value = TextFieldValue(digits, selection = androidx.compose.ui.text.TextRange(digits.length))
        if (digits.length == CODE_LENGTH) {
            verify()
        }
    }

    val bg = Brush.verticalGradient(listOf(Color(0xFF16121F), Bg))
    com.identia.app.ui.components.ScreenScaffold(brush = bg) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Logo(boxSize = 88.dp, radius = 26.dp, glyphSize = 40.dp, animatedRing = true)
                Spacer(Modifier.height(26.dp))
                Text("IdentIA", style = IdentiaTheme.type.brand, color = IdentiaTheme.colors.textPrimary)
                Spacer(Modifier.height(8.dp))
                Text(
                    "VERIFY · AUTHENTICATE · TRUST",
                    style = IdentiaTheme.type.monoTag,
                    color = IdentiaTheme.colors.primary,
                )
                Spacer(Modifier.height(16.dp))
                AlphaPill()
                Spacer(Modifier.height(34.dp))

                Text(
                    "ENTER ACCESS CODE",
                    style = IdentiaTheme.type.monoSm,
                    color = IdentiaTheme.colors.textMuted,
                )
                Spacer(Modifier.height(14.dp))

                // --- The OTP field: hidden BasicTextField overlaid by 6 cells ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer { translationX = shake.value },
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(9.dp),
                    ) {
                        for (i in 0 until CODE_LENGTH) {
                            val char = value.text.getOrNull(i)?.toString() ?: ""
                            val active = !isError && !isVerifying &&
                                (i == value.text.length || (value.text.length == CODE_LENGTH && i == CODE_LENGTH - 1))
                            DigitCell(
                                char = char,
                                active = active,
                                error = isError,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                    // Invisible, full-bleed input that captures the system keyboard.
                    BasicTextField(
                        value = value,
                        onValueChange = ::onValueChange,
                        modifier = Modifier
                            .matchParentSize()
                            .alpha(0f)
                            .focusRequester(focusRequester),
                        enabled = !isVerifying,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.NumberPassword,
                            imeAction = ImeAction.Done,
                        ),
                        cursorBrush = androidx.compose.ui.graphics.SolidColor(Color.Transparent),
                    )
                }

                Spacer(Modifier.height(12.dp))
                // Status line: error message or alpha hint.
                if (isError) {
                    Text(
                        "Invalid code. Try again.",
                        style = IdentiaTheme.type.mono,
                        color = IdentiaTheme.colors.error,
                        textAlign = TextAlign.Center,
                    )
                } else {
                    Text(
                        "Alpha access code: ${DemoState.DEMO_ACCESS_CODE}",
                        style = IdentiaTheme.type.mono,
                        color = IdentiaTheme.colors.textMutedAlt,
                        textAlign = TextAlign.Center,
                    )
                }

                Spacer(Modifier.height(22.dp))
                PrimaryButton(
                    text = if (isVerifying) "Verifying…" else "Verify",
                    onClick = {
                        if (value.text.length == CODE_LENGTH) verify()
                    },
                    modifier = Modifier.alpha(
                        if (isVerifying || value.text.length < CODE_LENGTH) 0.5f else 1f,
                    ),
                )
            }
            Text(
                "END-TO-END ENCRYPTED · v2.4.0-demo",
                style = IdentiaTheme.type.monoSm.copy(letterSpacing = androidx.compose.ui.unit.TextUnit.Unspecified),
                color = IdentiaTheme.colors.textMutedAlt,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 26.dp),
            )
        }
    }

    // Auto-focus so the keyboard pops on entry (mobile) / typing works (desktop).
    androidx.compose.runtime.LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun DigitCell(
    char: String,
    active: Boolean,
    error: Boolean,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(12.dp)
    val borderColor = when {
        error -> IdentiaTheme.colors.error
        active -> IdentiaTheme.colors.primary
        char.isNotEmpty() -> IdentiaTheme.colors.primary.copy(alpha = 0.45f)
        else -> IdentiaTheme.colors.borderStrong
    }
    val glowMod = if (active) {
        Modifier.glow(IdentiaTheme.colors.primary.copy(alpha = 0.6f), 12.dp, shape)
    } else if (error) {
        Modifier.glow(IdentiaTheme.colors.error.copy(alpha = 0.5f), 10.dp, shape)
    } else {
        Modifier
    }
    Box(
        modifier = modifier
            .height(56.dp)
            .then(glowMod)
            .background(CardInput, shape)
            .border(if (active || error || char.isNotEmpty()) 1.5.dp else 1.dp, borderColor, shape),
        contentAlignment = Alignment.Center,
    ) {
        if (char.isNotEmpty()) {
            Text(
                char,
                style = IdentiaTheme.type.statNumber,
                color = if (error) IdentiaTheme.colors.error else IdentiaTheme.colors.textPrimary,
            )
        }
    }
}
