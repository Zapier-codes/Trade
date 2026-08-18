package com.trade.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * D1/Slice 7 — `LocalTradeTheme` CompositionLocal + `TradeTheme { }`
 * wrapper Composable.
 *
 * `TradeColorTokens`' Slice-4 doc flagged this as deferred until both
 * `TradeThemeDark` and `TradeThemeLight` existed (Slice 5) — they now
 * do, and Slice 7 (glass primitives) is the first consumer, so it ships
 * here rather than being invented ad hoc inside `GlassSurface`.
 *
 * Bundles all three token groups (`colors`, `typography`, `spacing`)
 * behind one CompositionLocal so screens read
 * `LocalTradeTheme.current.colors.X` / `.typography.X` / `.spacing.X`
 * per Blueprint 3B.1, and never branch on light/dark themselves — only
 * `TradeTheme { }` itself picks `TradeThemeDark` vs `TradeThemeLight`.
 *
 * D1/Slice 9b adds [style]: which of the six [WidgetStyle] variants is
 * active (Blueprint 3B.1 "Widget Style variants", `HANDOVER.md` Section
 * 3E). Same rule applies — a primitive reads `LocalTradeTheme.current.style`
 * and switches its own internal rendering; screens themselves never branch
 * on it.
 */
data class TradeThemeInstance(
    val colors: TradeColorTokens,
    val typography: TradeTypographyTokens,
    val spacing: TradeSpacingTokens,
    val style: WidgetStyle = WidgetStyle.Glass,
)

private val defaultTradeTheme = TradeThemeInstance(
    colors = TradeThemeDark,
    typography = TradeTypography,
    spacing = TradeSpacing,
    style = WidgetStyle.Glass,
)

/**
 * No real default is meaningful outside a `TradeTheme { }` wrapper —
 * this exists so a screen previewed/rendered without one still gets
 * *something* (dark tokens) rather than crashing, per the same spirit
 * as Material3's own `LocalContentColor` fallback pattern.
 */
val LocalTradeTheme = staticCompositionLocalOf { defaultTradeTheme }

/**
 * Mount once near the app root (`MainActivity`/`AppShell`). Picks
 * `TradeThemeDark` or `TradeThemeLight` — typography/spacing are the
 * same either way (see their own files for why). `darkTheme` defaults
 * to the system setting; Slice 15's Demo/Live toggle is a separate,
 * unrelated concern and does not affect this parameter.
 *
 * D1/Slice 9b adds [style], defaulting to [WidgetStyle.Glass]. The caller
 * (`MainActivity`'s app-shell host, as of this slice) is expected to hoist
 * the selected style as mutable state and pass it through here so the
 * Settings > Appearance screen's picker takes effect app-wide — this
 * composable itself holds no state of its own, same as it holds none for
 * `darkTheme`. Per Section 3E, this is **in-memory only** for D1.S9b; real
 * persistence (DataStore) is R-phase scope.
 */
@Composable
fun TradeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    style: WidgetStyle = WidgetStyle.Glass,
    content: @Composable () -> Unit,
) {
    val instance = remember(darkTheme, style) {
        TradeThemeInstance(
            colors = if (darkTheme) TradeThemeDark else TradeThemeLight,
            typography = TradeTypography,
            spacing = TradeSpacing,
            style = style,
        )
    }
    CompositionLocalProvider(LocalTradeTheme provides instance, content = content)
}
