package com.llsit.joinsphere

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.llsit.joinsphere.feature.auth.LoginScreen
import com.llsit.joinsphere.feature.onboarding.OnboardingScreen
import com.llsit.navigation.AppNavigator
import com.llsit.navigation.AuthKey
import com.llsit.navigation.MainKey
import com.llsit.navigation.OnboardingKey
import com.llsit.navigation.SplashKey

@Composable
fun RootNavDisplay(navigator: AppNavigator) {
    NavDisplay(
        backStack = navigator.rootBackStack,
        onBack = { navigator.goBack() },
        entryProvider = entryProvider {

            // ── Onboarding flow ───────────────────────────────────────────────
            entry<SplashKey> {
                SplashScreen { startOnboarding ->
                    navigator.proceedFromSplash(startOnboarding)
                }
            }

            entry<OnboardingKey> {
                OnboardingScreen(onComplete = { navigator.completeOnboarding() })
            }

            entry<AuthKey> {
                LoginScreen(onLogin = { navigator.loginSuccess() })
            }

            // ── Main scaffold (tabs) ──────────────────────────────────────────
            entry<MainKey> { MainScaffold(navigator = navigator) }
        },
    )
}