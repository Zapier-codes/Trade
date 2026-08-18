package com.trade.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.trade.app.presentation.AppShellUiState
import com.trade.app.presentation.AppShellViewModel
import com.trade.core.navigation.TradeNavHost

/**
 * Single-activity host. As of Slice 3, hosts the full [TradeNavHost] (every
 * planned route registered in `core-navigation`'s `TradeRoutes`, each an
 * empty placeholder screen) instead of Slice 1b/2's single hardcoded
 * screen.
 *
 * Slice 2's presentation -> domain -> data chain ([AppShellViewModel])
 * still runs, its build-info string now feeds the route directory's
 * subtitle rather than being the whole screen — see [TradeAppShellHost].
 */
class MainActivity : ComponentActivity() {

    private val viewModel: AppShellViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by viewModel.uiState.collectAsState()
            TradeAppShellHost(state)
        }
    }
}

@Composable
private fun TradeAppShellHost(state: AppShellUiState) {
    // core-theme's TradeTheme wrapper + ThemeEventBus land in Slices 4-14.
    // This is a deliberately unstyled host so it's obvious it predates the
    // theming engine.
    Surface(modifier = Modifier) {
        val subtitle = when (state) {
            AppShellUiState.Loading -> "TRADE — loading build info..."
            is AppShellUiState.Loaded -> "TRADE ${state.versionName} (demo mode: ${state.isDemoMode})"
        }
        TradeNavHost(buildInfoSubtitle = subtitle)
    }
}
