package com.trade.core.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * D1/Slice 6 — Spacing token group ("Typography + spacing tokens" per
 * the D1/R1 phase table; spacing itself isn't broken out as its own row
 * in Blueprint 3B.1's token-group table, but every screen from Phase D2
 * onward needs a shared spacing scale to lay out consistently, so it
 * ships alongside typography in this slice rather than being invented
 * ad hoc per screen).
 *
 * One instance, no dark/light split — same reasoning as
 * [TradeTypographyTokens]. This is a **layout/gap** scale (padding,
 * margins, gaps between elements) — not to be confused with the
 * `radius.sm/md/lg/pill` corner-radius group from Blueprint 3B.1, which
 * is separate and out of this slice's scope.
 *
 * Base unit is 4dp, standard for Compose/Material-based layouts, giving
 * every step a clean relationship to the others (`md` = 4×`xs`, etc).
 */
data class TradeSpacingTokens(
    /** Tightest gap — icon-to-label, chip internal padding. */
    val xs: Dp,
    /** Small gap — related items within a tight group. */
    val sm: Dp,
    /** Default gap — most padding/margins, the scale's base. */
    val md: Dp,
    /** Comfortable gap — between distinct elements in a card. */
    val lg: Dp,
    /** Section gap — between cards, between major screen sections. */
    val xl: Dp,
    /** Largest gap — top-level screen padding, hero section spacing. */
    val xxl: Dp,
)

/**
 * The one `TradeSpacingTokens` instance — see class doc for why there's
 * no dark/light split.
 */
val TradeSpacing = TradeSpacingTokens(
    xs = 4.dp,
    sm = 8.dp,
    md = 16.dp,
    lg = 24.dp,
    xl = 32.dp,
    xxl = 48.dp,
)
