package com.trade.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.trade.core.theme.WidgetStyle
import com.trade.core.ui.AppearanceSettingsScreen

/**
 * D1/Slice 3 — Navigation graph. All planned routes ([TradeRoutes.all])
 * wired here via Compose Navigation, every one resolving to
 * [EmptyRouteScreen] for now.
 *
 * Start destination is [TradeRoutes.ROUTE_DIRECTORY] (see
 * [RouteDirectoryScreen]) rather than [TradeRoutes.welcome] — deciding the
 * real app entry flow (welcome vs. returning-user auth vs. dashboard) is
 * D2 scope, not this slice's.
 *
 * **D1/Slice 9b:** [TradeRoutes.appearanceSettings] is the first route to
 * get real content instead of [EmptyRouteScreen] (`AppearanceSettingsScreen`,
 * `core-ui`) — wired explicitly before the generic placeholder loop and
 * excluded from it, same pattern [ROUTE_DIRECTORY] already uses for its own
 * special-cased destination. Per the D2+ convention already documented in
 * `TradeRoute.kt`, every future slice that ships a route's real screen
 * should do the same: add its own `composable(route.path) { ... }` above
 * the loop and filter that path out of it, rather than duplicating the
 * route id.
 *
 * @param buildInfoSubtitle passed straight through to [RouteDirectoryScreen]'s
 *   subtitle line — keeps Slice 2's presentation -> domain -> data chain
 *   visibly wired into the new nav graph rather than orphaning it.
 * @param widgetStyle the app-wide active [WidgetStyle] (hoisted by the
 *   caller — see `MainActivity`'s `TradeAppShellHost`). Threaded through to
 *   [AppearanceSettingsScreen] so its picker shows the real current
 *   selection, not a locally-reset default.
 * @param onWidgetStyleSelected fired when the user taps a style row;
 *   caller is expected to update the same hoisted state that feeds
 *   [widgetStyle] and `TradeTheme`'s `style` param, so the change reskins
 *   the whole app, not just this screen.
 */
@Composable
fun TradeNavHost(
    buildInfoSubtitle: String,
    widgetStyle: WidgetStyle,
    onWidgetStyleSelected: (WidgetStyle) -> Unit,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navController, startDestination = TradeRoutes.ROUTE_DIRECTORY) {
        composable(TradeRoutes.ROUTE_DIRECTORY) {
            RouteDirectoryScreen(
                subtitle = buildInfoSubtitle,
                onRouteSelected = { route -> navController.navigate(route.path) },
            )
        }
        composable(TradeRoutes.appearanceSettings.path) {
            AppearanceSettingsScreen(
                currentStyle = widgetStyle,
                onStyleSelected = onWidgetStyleSelected,
                onBack = {
                    navController.popBackStack(TradeRoutes.ROUTE_DIRECTORY, inclusive = false)
                },
            )
        }
        TradeRoutes.all
            .filter { it.path != TradeRoutes.appearanceSettings.path }
            .forEach { route ->
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
