package com.trade.core.theme

import androidx.compose.ui.graphics.Color

/**
 * D1/Slice 5 — `TradeThemeLight` full color token set (Blueprint 3B.1).
 *
 * Per Blueprint 3B.4 ("Dark/Light Parity Rule"), this is a **separate,
 * independently-designed** token set, not `TradeThemeDark` lightened.
 * Palette intent, per Blueprint 3B.2's light-mode glass description:
 * "bright frosted-white translucent surface, warm hairline border, blur
 * softens background color instead of glowing — no neon, more 'misted
 * glass' than 'sci-fi panel.'" Everywhere the dark set reaches for glow
 * or saturated fill, this set reaches for shadow depth and hairline
 * color instead (Blueprint 3B.4) — `ambientGlow` here is deliberately a
 * soft neutral shadow tone, not a bright accent, since `ThemeReactor`
 * (Slices 10-11) drives light mode's *shadow* reactions from it rather
 * than a literal glow.
 *
 * Same field set/order as `TradeThemeDark` for easy side-by-side diffing
 * — see `TradeColorTokens` for what each field means.
 */
val TradeThemeLight = TradeColorTokens(
    // ---- Base surfaces ----
    background = Color(0xFFF7F8FA),
    surface = Color(0xFFFFFFFF),
    surfaceElevated = Color(0xFFFFFFFF),
    // Bright frosted-white translucent glass fill (~80% opacity) — the
    // "bright frosted-white translucent surface" from Blueprint 3B.2.
    surfaceGlass = Color(0xCCFFFFFF),
    // "Warm hairline border" (Blueprint 3B.2) — low-opacity warm neutral,
    // not the cool-white stroke dark mode uses.
    borderGlassStroke = Color(0x332B2118),
    divider = Color(0x1A0D0D0D),
    overlayScrim = Color(0x66000000),

    // ---- Accents & semantic signal colors ----
    accentPrimary = Color(0xFF3A5FDB),
    accentSecondary = Color(0xFF7C3AED),
    // Distinct from accentPrimary, same role as dark mode's accentSignal
    // (AI-agent/signal surfaces read as their own category), deepened
    // slightly from the dark-mode cyan so it holds contrast on white.
    accentSignal = Color(0xFF0E93A8),
    // Idle/resting state that ThemeReactor animates around in light
    // mode. Per Blueprint 3B.4, light mode favors shadow depth over
    // glow, so this is a soft cool-neutral shadow tone rather than a
    // dimmed accent color — it tints hairlines/shadows, not a glow.
    ambientGlow = Color(0xFFD8DEE9),
    onAccent = Color(0xFFFFFFFF),

    // ---- Trading semantics ----
    // Kept close to dark mode's positive/negative/warning hues (trading
    // color meaning should stay recognizable across modes) but deepened
    // for AA contrast against white/near-white surfaces.
    positive = Color(0xFF1A9850),
    negative = Color(0xFFD93A3A),
    // Warm gold, same special-occasion role as dark mode's depositGold —
    // pairs with the "warm cream sweep" DepositConfirmed effect.
    depositGold = Color(0xFFB8862E),
    warning = Color(0xFFC17D0A),
    // "Thin warm-red hairline" (Blueprint 3B.3, ErrorOccurred, light-mode
    // effect) — distinct from `negative`, same relationship as dark mode.
    error = Color(0xFFC2492F),

    // ---- Text ----
    textPrimary = Color(0xFF14181F),
    textSecondary = Color(0xFF4B5563),
    textTertiary = Color(0xFF7A8494),
    textDisabled = Color(0xFFAFB6C0),
)
