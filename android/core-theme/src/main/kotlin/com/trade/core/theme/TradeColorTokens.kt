package com.trade.core.theme

import androidx.compose.ui.graphics.Color

/**
 * D1/Slice 4 — Color token group (Blueprint Section 3B.1).
 *
 * The full `ThemeTokens` object described in the blueprint also has
 * Elevation/Blur, Motion, Typography, and Radius groups — those are
 * Slice 6 scope ("Typography + spacing tokens") and later, not this one.
 * This file defines only the **Color** group's shape, so both
 * `TradeThemeDark` (this slice) and `TradeThemeLight` (Slice 5) implement
 * the same fields — screens read `LocalTradeTheme.current.colors.X` and
 * never branch on light/dark themselves (Blueprint 3B.1).
 *
 * Field set is broader than the blueprint's illustrative "Examples"
 * column (`surface`, `surfaceGlass`, `accentPrimary`, `accentSignal`,
 * `ambientGlow`) — those five are marked below — because a "full token
 * set" needs basics (background/text/dividers) no screen can ship
 * without, plus the semantic trading colors (positive/negative) implied
 * throughout the blueprint's user journeys and fee/result displays
 * (Section 10.1). Kept intentionally flat and named, not nested, so a
 * missing token is a compile error, not a silent runtime fallback.
 *
 * **Not in this slice's scope, flagged for later:**
 * - No `LocalTradeTheme` `CompositionLocal` or `TradeTheme { }` wrapper
 *   Composable yet — that needs both `TradeThemeDark` *and*
 *   `TradeThemeLight` to exist first (Slice 5), so it's deferred rather
 *   than guessed at half-built. See `docs/HANDOVER.md` Section 6.
 * - `ambientGlow` here is each theme's *idle/default* value only. Slices
 *   10-11 (`ThemeEventBus`/`ThemeReactor`) animate it at runtime in
 *   response to `ThemeEvent`s — this token is the resting state that
 *   animation returns to, not a static color screens read directly once
 *   the reactor exists.
 */
data class TradeColorTokens(
    // ---- Base surfaces ----
    /** App background, behind all glass/elevated content. */
    val background: Color,
    /** Blueprint example token: opaque surface (cards, sheets not using glass). */
    val surface: Color,
    /** Elevated opaque surface, one step above [surface] (e.g. sheet over screen). */
    val surfaceElevated: Color,
    /** Blueprint example token: translucent glass surface fill, paired with [borderGlassStroke]. */
    val surfaceGlass: Color,
    /** Hairline/border stroke on glass primitives — cool-white in dark mode per Blueprint 3B.2. */
    val borderGlassStroke: Color,
    /** Non-glass divider/hairline between list rows, sections, etc. */
    val divider: Color,
    /** Scrim behind modals/sheets/dialogs. */
    val overlayScrim: Color,

    // ---- Accents & semantic signal colors ----
    /** Blueprint example token: primary brand accent (CTAs, active states). */
    val accentPrimary: Color,
    /** Secondary accent, for less-emphasized interactive elements. */
    val accentSecondary: Color,
    /** Blueprint example token: AI-agent/signal accent (agent panels, signal cards, confidence indicators). */
    val accentSignal: Color,
    /** Blueprint example token: idle ambient glow color that `ThemeReactor` animates away from/back to. */
    val ambientGlow: Color,
    /** Content color for text/icons rendered on top of a filled accent surface. */
    val onAccent: Color,

    // ---- Trading semantics ----
    /** Gains, buy side, positive P&L, `TradeExecuted(side = buy)` resting tint. */
    val positive: Color,
    /** Losses, sell side, negative P&L, `TradeExecuted(side = sell)` resting tint. */
    val negative: Color,
    /** Deposit/bonus-credit accent — gold, per `DepositConfirmed`'s "gold ambient sweep" (Blueprint 3B.3). */
    val depositGold: Color,
    /** General caution (e.g. leverage risk warnings, non-blocking form warnings). */
    val warning: Color,
    /** `ErrorOccurred`'s "muted red-orange" resting tint (Blueprint 3B.3) — distinct from [negative], which is a trade-result color, not a failure color. */
    val error: Color,

    // ---- Text ----
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textDisabled: Color,
)
