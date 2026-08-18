package com.trade.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.trade.app.presentation.AppShellUiState
import com.trade.app.presentation.AppShellViewModel

/**
 * Single-activity host. Real navigation graph (all planned routes as empty
 * Composables) is Slice 3 — this file intentionally renders one placeholder
 * screen only.
 *
 * As of Slice 2, that screen is wired through [AppShellViewModel] to
 * demonstrate the presentation -> domain -> data contract chain every
 * later feature screen should follow, rather than the hardcoded string
 * Slice 1b shipped.
 */
class MainActivity : ComponentActivity() {

    private val viewModel: AppShellViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by viewModel.uiState.collectAsState()
            TradeAppShellPlaceholder(state)
        }
    }
}

@Composable
private fun TradeAppShellPlaceholder(state: AppShellUiState) {
    // core-theme's TradeTheme wrapper + ThemeEventBus land in Slices 4-14.
    // This is a deliberately unstyled placeholder so it's obvious it predates
    // the theming engine.
    Surface(modifier = Modifier) {
        val text = when (state) {
            AppShellUiState.Loading ->
                "TRADE — loading build info..."
            is AppShellUiState.Loaded ->
                "TRADE ${state.versionName} (demo mode: ${state.isDemoMode}). Navigation graph: Slice 3."
        }
        Text(text = text)
    }
}
