package com.trade.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.trade.core.theme.LocalTradeAmbient
import com.trade.core.theme.ThemeEvent
import com.trade.core.theme.ThemeEventBus
import com.trade.core.theme.TradeSide

/**
 * D1/Slice 3 — start destination of [TradeNavHost].
 *
 * Not a product screen: a QA aid so every registered route can be reached
 * by tapping on a real device, without deep-link tooling, matching the
 * project's phone/Termux review workflow (see `docs/HANDOVER.md` Section
 * 3, "keeps each session's diff reviewable"). Real app entry-point routing
 * (welcome vs. returning-user auth vs. dashboard) is decided in D2 — this
 * screen is intentionally not that decision.
 *
 * Grouped by topic in [TradeRoutes.all] declaration order so it doubles as
 * a quick visual cross-check against `docs/HANDOVER.md` Section 4's phase
 * tables.
 *
 * **D1/Slice 11 addition:** [ThemeReactorTestSection] at the top — seven
 * buttons (two for `TradeExecuted`'s Buy/Sell variants, one each for the
 * other five [ThemeEvent] types), plus a swatch reading
 * [LocalTradeAmbient.current]'s live-animated `ambientGlow`. Added beyond
 * this slice's literal name ("ThemeReactor — ambient glow") for the same
 * reason Slice 3's own doc above flags this screen's existence at all:
 * there is currently no Gradle wrapper in the sandbox this project is
 * built in (every prior slice's Known Issues entry notes "not
 * independently buildable... reviewed by hand only"), so a demo trigger
 * reachable from the one screen every route already funnels through is
 * the only way the human can actually *see* Slice 11 work on a real
 * device once they pull this patch, rather than trusting a code review
 * alone. Deliberately plain Material3 `Button`s/`Box`, matching this
 * screen's existing "not yet reskinned" character (see the dependencies
 * doc in `build.gradle.kts`) rather than reaching for `core-ui`'s glass
 * primitives here.
 */
@Composable
fun RouteDirectoryScreen(
    subtitle: String,
    onRouteSelected: (TradeRoute) -> Unit,
) {
    val grouped = TradeRoutes.all.groupBy { it.group }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("TRADE — Route Directory") })
        },
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            item {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp),
                )
            }
            item { ThemeReactorTestSection() }
            grouped.forEach { (group, routes) ->
                item {
                    Text(
                        text = group,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                }
                items(routes) { route ->
                    ListItem(
                        headlineContent = { Text(route.title) },
                        supportingContent = { Text(route.slice) },
                        modifier = Modifier.clickable { onRouteSelected(route) },
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

/**
 * D1/Slice 11 QA aid — see [RouteDirectoryScreen]'s class doc for why this
 * exists on this particular screen. [ThemeEventBus.tryEmit] (non-suspending
 * — "for call sites without a coroutine scope handy," per that function's
 * own doc) is the right call here: a `Button.onClick` lambda isn't a
 * coroutine scope, and firing a single UI test event never needs the
 * suspending [ThemeEventBus.emit] overload's backpressure guarantee.
 *
 * The swatch's color comes from [LocalTradeAmbient] — which only exists if
 * a [com.trade.core.theme.ThemeReactor] is mounted above this screen in the
 * composition (it is, as of this slice — see `MainActivity.kt`). If this
 * section is ever previewed/hosted without one, [LocalTradeAmbient]'s own
 * fallback default (a static, non-animating idle color) still renders
 * something reasonable rather than crashing — see that `CompositionLocal`'s
 * doc comment.
 */
@Composable
private fun ThemeReactorTestSection() {
    val ambient = LocalTradeAmbient.current

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(text = "ThemeReactor test (D1/Slice 11)", style = MaterialTheme.typography.titleSmall)
        Text(
            text = "Ambient glow swatch below animates on tap — pulses toward the " +
                "event's color, then eases back to the current theme's idle glow.",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(ambient.ambientGlow)
                .padding(bottom = 8.dp),
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(onClick = { ThemeEventBus.tryEmit(ThemeEvent.TradeExecuted(TradeSide.Buy)) }) {
                Text("Buy fill")
            }
            Button(onClick = { ThemeEventBus.tryEmit(ThemeEvent.TradeExecuted(TradeSide.Sell)) }) {
                Text("Sell fill")
            }
            Button(onClick = { ThemeEventBus.tryEmit(ThemeEvent.DepositConfirmed) }) {
                Text("Deposit")
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 8.dp),
        ) {
            Button(onClick = { ThemeEventBus.tryEmit(ThemeEvent.AISignalFired(confidence = 0.87f)) }) {
                Text("AI signal")
            }
            Button(onClick = { ThemeEventBus.tryEmit(ThemeEvent.WithdrawalBroadcast) }) {
                Text("Withdrawal")
            }
            Button(onClick = { ThemeEventBus.tryEmit(ThemeEvent.ErrorOccurred) }) {
                Text("Error")
            }
            Button(onClick = { ThemeEventBus.tryEmit(ThemeEvent.AgentStatusChange(active = true)) }) {
                Text("Agent tick")
            }
        }
    }
}
