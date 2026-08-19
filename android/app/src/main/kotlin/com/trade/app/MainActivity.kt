package com.trade.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.trade.app.presentation.AppShellUiState
import com.trade.app.presentation.AppShellViewModel
import com.trade.core.navigation.TradeNavHost
import com.trade.core.theme.SoundReactor
import com.trade.core.theme.TradeTheme
import com.trade.core.theme.WidgetStyle

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
    // D1/Slice 9b: TradeTheme (built Slice 7, never actually mounted until
    // now) is wired here for the first time. It was safe to leave unmounted
    // through Slices 7-9 because GlassSurface/GlassCard's LocalTradeTheme
    // read falls back to its own dark-mode default when nothing provides
    // it (see LocalTradeTheme.kt) — but this slice's whole point is a
    // picker that visibly reskins the app, which needs a real Provider in
    // the tree with mutable state above TradeNavHost, not the static
    // fallback. Flagged here rather than silently left implicit, per
    // Section 1's non-negotiable rule: this was a blocking dependency for
    // D1.S9b specifically, the same way Slice 7 flagged mounting
    // LocalTradeTheme/TradeTheme itself as a blocking dependency it built
    // ahead of its "official" slice.
    //
    // `style` is hoisted here, in-memory only (Section 3E: no persistence
    // in this slice) — process death resets to WidgetStyle.Glass. R-phase
    // replaces this `remember { mutableStateOf(...) }` with a real
    // DataStore-backed value, same D-phase/R-phase split as everything
    // else in this file.
    var style by remember { mutableStateOf(WidgetStyle.Glass) }

    // D1/Slice 10 — SoundReactor (Blueprint 1C.2) is mounted here, "at app
    // root," per that section's own wording. `remember(context)` rather
    // than a plain top-level singleton (unlike ThemeEventBus) because it
    // holds a Context-scoped SoundPool that must be released — see
    // SoundReactor.kt's class doc for why it isn't an `object`.
    //
    // LaunchedEffect(soundReactor) starts the (suspend-forever) collection
    // loop; it's cancelled automatically if `soundReactor` identity ever
    // changed (it won't, `remember` keys are stable here) or on
    // composition leaving. DisposableEffect separately releases the
    // SoundPool on final teardown — collection stopping and the pool
    // being released are two different lifecycle events, so they're two
    // different effects rather than one that tries to do both.
    val context = LocalContext.current
    val soundReactor = remember(context) { SoundReactor(context) }
    LaunchedEffect(soundReactor) {
        soundReactor.start()
    }
    DisposableEffect(soundReactor) {
        onDispose { soundReactor.release() }
    }

    TradeTheme(style = style) {
        Surface(modifier = Modifier) {
            val subtitle = when (state) {
                AppShellUiState.Loading -> "TRADE — loading build info..."
                is AppShellUiState.Loaded -> "TRADE ${state.versionName} (demo mode: ${state.isDemoMode})"
            }
            TradeNavHost(
                buildInfoSubtitle = subtitle,
                widgetStyle = style,
                onWidgetStyleSelected = { style = it },
            )
        }
    }
}
