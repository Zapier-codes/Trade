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
 */
data class TradeThemeInstance(
    val colors: TradeColorTokens,
    val typography: TradeTypographyTokens,
    val spacing: TradeSpacingTokens,
)

private val defaultTradeTheme = TradeThemeInstance(
    colors = TradeThemeDark,
    typography = TradeTypography,
    spacing = TradeSpacing,
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
 */
@Composable
fun TradeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val instance = remember(darkTheme) {
        TradeThemeInstance(
            colors = if (darkTheme) TradeThemeDark else TradeThemeLight,
            typography = TradeTypography,
            spacing = TradeSpacing,
        )
    }
    CompositionLocalProvider(LocalTradeTheme provides instance, content = content)
}
