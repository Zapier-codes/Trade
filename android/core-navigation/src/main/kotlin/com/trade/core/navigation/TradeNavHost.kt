package com.trade.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * D1/Slice 3 — Navigation graph. All planned routes ([TradeRoutes.all])
 * wired here via Compose Navigation, every one resolving to
 * [EmptyRouteScreen] for now.
 *
 * Start destination is [TradeRoutes.ROUTE_DIRECTORY] (see
 * [RouteDirectoryScreen]) rather than [TradeRoutes.welcome] — deciding the
 * real app entry flow (welcome vs. returning-user auth vs. straight to
 * dashboard) is D2 scope, not this slice's.
 *
 * @param buildInfoSubtitle passed straight through to [RouteDirectoryScreen]'s
 *   subtitle line — keeps Slice 2's presentation -> domain -> data chain
 *   visibly wired into the new nav graph rather than orphaning it.
 */
@Composable
fun TradeNavHost(
    buildInfoSubtitle: String,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navController, startDestination = TradeRoutes.ROUTE_DIRECTORY) {
        composable(TradeRoutes.ROUTE_DIRECTORY) {
            RouteDirectoryScreen(
                subtitle = buildInfoSubtitle,
                onRouteSelected = { route -> navController.navigate(route.path) },
            )
        }
        TradeRoutes.all.forEach { route ->
            composable(route.path) {
                EmptyRouteScreen(
                    route = route,
                    onBackToDirectory = {
                        navController.popBackStack(TradeRoutes.ROUTE_DIRECTORY, inclusive = false)
                    },
                )
            }
        }
    }
}
