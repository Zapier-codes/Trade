package com.trade.core.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
