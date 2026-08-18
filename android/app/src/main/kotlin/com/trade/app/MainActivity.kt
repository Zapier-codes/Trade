package com.trade.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Single-activity host. Real navigation graph (all planned routes as empty
 * Composables) is Slice 3 — this file intentionally renders one placeholder
 * screen only, so Slice 1b stays scoped to "app boots and shows something."
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TradeAppShellPlaceholder()
        }
    }
}

@Composable
private fun TradeAppShellPlaceholder() {
    // core-theme's TradeTheme wrapper + ThemeEventBus land in Slices 4-14.
    // This is a deliberately unstyled placeholder so it's obvious it predates
    // the theming engine.
    Surface(modifier = Modifier) {
        Text(text = "TRADE — D1/Slice 1b scaffold. Navigation graph: Slice 3.")
    }
}
