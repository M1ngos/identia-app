package com.identia.app.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.identia.app.core.theme.Bg
import com.identia.app.feature.auth.AuthEntryScreen
import com.identia.app.feature.face.FaceCameraScreen
import com.identia.app.feature.face.FaceMatchScreen
import com.identia.app.feature.face.FaceScanningScreen
import com.identia.app.feature.home.HomeScreen
import com.identia.app.feature.logs.LogsScreen
import com.identia.app.feature.settings.SettingsScreen
import com.identia.app.feature.verify.CaptureIdScreen
import com.identia.app.feature.verify.LivenessScreen
import com.identia.app.feature.verify.SelfieScreen
import com.identia.app.feature.verify.VerifyResultScreen
import com.identia.app.feature.verify.VerifyStartScreen
import com.identia.app.state.LocalDemoState
import com.identia.app.ui.components.AlphaStrip
import com.identia.app.ui.components.BottomNav

@Composable
fun AppRoot() {
    val nav = rememberNavController()
    val demo = LocalDemoState.current
    val backStackEntry by nav.currentBackStackEntryAsState()
    val dest = backStackEntry?.destination

    val selectedTab = when {
        dest?.hasRoute(HomeRoute::class) == true -> Tab.Home
        dest?.hasRoute(LogsRoute::class) == true -> Tab.Logs
        else -> null
    }
    val showBottomBar = selectedTab != null

    fun logout() {
        demo.logOut()
        nav.navigate(AuthRoute) {
            popUpTo(nav.graph.id) { inclusive = true }
        }
    }

    fun switchTab(tab: Tab) {
        val route: Any = when (tab) {
            Tab.Home -> HomeRoute
            Tab.Verify -> VerifyStartRoute
            Tab.Face -> FaceCameraRoute
            Tab.Logs -> LogsRoute
        }
        nav.navigate(route) {
            popUpTo(nav.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        containerColor = Bg,
        bottomBar = {
            if (showBottomBar) {
                BottomNav(selected = selectedTab, onSelect = ::switchTab)
            }
        },
    ) { padding ->
        val contentModifier = if (showBottomBar) {
            Modifier.fillMaxSize().padding(bottom = padding.calculateBottomPadding())
        } else {
            Modifier.fillMaxSize()
        }
        Column(modifier = contentModifier) {
            // Persistent alpha strip on the authenticated tab screens.
//            if (showBottomBar) {
//                AlphaStrip()
//            }
            NavHost(
                navController = nav,
                startDestination = AuthRoute,
                modifier = Modifier.fillMaxSize(),
            ) {
            composable<AuthRoute> {
                AuthEntryScreen(onEnter = {
                    nav.navigate(HomeRoute) {
                        popUpTo<AuthRoute> { inclusive = true }
                    }
                })
            }

            // Tab pages
            composable<HomeRoute> {
                HomeScreen(
                    onModule = { tab -> switchTab(tab) },
                    onSettings = { nav.navigate(SettingsRoute) },
                )
            }
            composable<LogsRoute> { LogsScreen() }
            composable<SettingsRoute> {
                SettingsScreen(onBack = { nav.popBackStack() }, onLogout = ::logout)
            }

            // Identity-verification sub-flow
            composable<VerifyStartRoute> {
                VerifyStartScreen(
                    onBack = { nav.popBackStack() },
                    onStart = { nav.navigate(CaptureRoute(front = true)) },
                )
            }
            composable<CaptureRoute> { entry ->
                val route = entry.toRoute<CaptureRoute>()
                CaptureIdScreen(
                    front = route.front,
                    onBack = { nav.popBackStack() },
                    onCapture = {
                        if (route.front) {
                            nav.navigate(CaptureRoute(front = false))
                        } else {
                            nav.navigate(SelfieRoute)
                        }
                    },
                )
            }
            composable<SelfieRoute> {
                SelfieScreen(
                    onBack = { nav.popBackStack() },
                    onCapture = { nav.navigate(LivenessRoute) },
                )
            }
            composable<LivenessRoute> {
                LivenessScreen(
                    onBack = { nav.popBackStack() },
                    onComplete = { failed ->
                        nav.navigate(VerifyResultRoute(failed = failed)) {
                            popUpTo<VerifyStartRoute> { inclusive = true }
                        }
                    },
                )
            }
            composable<VerifyResultRoute> { entry ->
                val route = entry.toRoute<VerifyResultRoute>()
                VerifyResultScreen(
                    failed = route.failed,
                    onDone = {
                        nav.navigate(HomeRoute) { popUpTo<HomeRoute> { inclusive = true } }
                    },
                    onRetry = { nav.navigate(VerifyStartRoute) },
                )
            }

            // Face-authentication sub-flow
            composable<FaceCameraRoute> {
                FaceCameraScreen(
                    onBack = { nav.popBackStack() },
                    onScan = { nav.navigate(FaceScanningRoute) },
                )
            }
            composable<FaceScanningRoute> {
                FaceScanningScreen(
                    onComplete = {
                        nav.navigate(FaceMatchRoute(confidence = 99.2f)) {
                            popUpTo<FaceScanningRoute> { inclusive = true }
                        }
                    },
                )
            }
            composable<FaceMatchRoute> { entry ->
                val route = entry.toRoute<FaceMatchRoute>()
                FaceMatchScreen(
                    confidence = route.confidence,
                    onContinue = {
                        nav.navigate(HomeRoute) { popUpTo<HomeRoute> { inclusive = true } }
                    },
                )
            }
            }
        }
    }
}
