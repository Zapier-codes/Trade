package com.trade.core.theme

import androidx.compose.ui.graphics.Color

/**
 * D1/Slice 4 — `TradeThemeDark` full color token set (Blueprint 3B.1).
 *
 * Palette intent, per Blueprint 3B.2's dark-mode glass description:
 * "near-black translucent surface, soft cool-white border stroke, subtle
 * inner glow, blur pulls in ambient accent color." Everything below reads
 * against a near-black base with a cool (blue-leaning), not warm-leaning,
 * neutral ramp — gold/warm tones are reserved for the deposit/bonus accent
 * specifically (see `depositGold`), matching `DepositConfirmed`'s "gold
 * ambient sweep" being a distinct, special-occasion event color rather
 * than part of the everyday neutral palette.
 *
 * `TradeThemeLight` (Slice 5) is a **separate, independently-designed**
 * token set — per Blueprint 3B.4 ("Dark/Light Parity Rule"), light mode is
 * not this palette lightened, it favors shadow depth and hairlines over
 * glow, so it needs its own pass rather than a mechanical inversion.
 */
val TradeThemeDark = TradeColorTokens(
    // ---- Base surfaces ----
    background = Color(0xFF05070A),
    surface = Color(0xFF0D1117),
    surfaceElevated = Color(0xFF141B24),
    // Translucent near-black glass fill (~72% opacity) — the "near-black
    // translucent surface" from Blueprint 3B.2, not the opaque `surface`
    // above.
    surfaceGlass = Color(0xB80B0E13),
    // "Soft cool-white border stroke" (Blueprint 3B.2) — low-opacity cool
    // white, not a saturated color.
    borderGlassStroke = Color(0x33E8F1FF),
    divider = Color(0x1FFFFFFF),
    overlayScrim = Color(0xB3000000),

    // ---- Accents & semantic signal colors ----
    accentPrimary = Color(0xFF4C7CFF),
    accentSecondary = Color(0xFF8B5CF6),
    // Distinct from accentPrimary so AI-agent/signal surfaces (agent
    // status panel, signal feed, confidence indicators) read as their own
    // category at a glance, not just "more blue."
    accentSignal = Color(0xFF22D3EE),
    // Idle/resting ambient glow — a dimmed accentPrimary. ThemeReactor
    // (Slices 10-11) animates away from and back to this value; it is not
    // meant to read as a strong color on its own.
    ambientGlow = Color(0xFF1E3A5F),
    onAccent = Color(0xFFFFFFFF),

    // ---- Trading semantics ----
    positive = Color(0xFF22C55E),
    negative = Color(0xFFEF4444),
    // Warm gold, deliberately the one warm-leaning color in an otherwise
    // cool dark palette — see file header.
    depositGold = Color(0xFFF5C451),
    warning = Color(0xFFF5A623),
    // "Muted red-orange" (Blueprint 3B.3, ErrorOccurred) — a failure/error
    // color, intentionally desaturated relative to `negative` so a failed
    // withdrawal doesn't visually read identically to a losing trade.
    error = Color(0xFFE0654B),

    // ---- Text ----
    textPrimary = Color(0xFFF5F7FA),
    textSecondary = Color(0xFFA9B4C0),
    textTertiary = Color(0xFF6B7684),
    textDisabled = Color(0xFF4A5561),
)
