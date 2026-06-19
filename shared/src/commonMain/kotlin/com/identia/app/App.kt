package com.identia.app

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import com.identia.app.core.theme.AppTheme
import com.identia.app.navigation.AppRoot
import com.identia.app.state.LocalDemoState
import com.identia.app.state.rememberDemoState

@Composable
@Preview
fun App() {
    AppTheme {
        CompositionLocalProvider(LocalDemoState provides rememberDemoState()) {
            AppRoot()
        }
    }
}
