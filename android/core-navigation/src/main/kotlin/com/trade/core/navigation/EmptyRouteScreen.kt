package com.trade.core.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * D1/Slice 3 — the single "empty Composable" every registered [TradeRoute]
 * resolves to for now. Deliberately unstyled (no `core-theme` tokens, no
 * `core-ui` glass primitives — those don't exist yet, they land Slices 4-9)
 * so it's obvious on sight that a screen predates its real feature-module
 * implementation.
 *
 * When a feature module ships a route's real screen (D2+), that module's
 * own Composable replaces this in [TradeNavHost] and this function is no
 * longer referenced for that route — nothing else needs to change here.
 */
@Composable
fun EmptyRouteScreen(
    route: TradeRoute,
    onBackToDirectory: () -> Unit,
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        ) {
            Text(text = route.title, style = MaterialTheme.typography.headlineSmall)
            Text(text = route.group, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Real content lands in ${route.slice}. Route: \"${route.path}\".",
                style = MaterialTheme.typography.bodySmall,
            )
            Button(onClick = onBackToDirectory) {
                Text("Back to route directory")
            }
        }
    }
}
