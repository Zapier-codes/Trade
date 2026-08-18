package com.trade.core.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * D1/Slice 6 — Typography token group (Blueprint 3B.1: "Typography |
 * display/heading/body/caption scale + weight").
 *
 * Unlike [TradeColorTokens], this group has **one instance, not two**
 * — the phase table marks R1's typography focus as "— (rarely changes;
 * verify only)", and nothing in the blueprint's Dark/Light Parity Rule
 * (3B.4) applies to type scale, only to color/glow/shadow expression.
 * Screens read `LocalTradeTypography.current.X` the same way they read
 * `LocalTradeTheme.current.colors.X` — no light/dark branching here
 * either, there's just nothing to branch on.
 *
 * Scale is a standard type-scale ratio (~1.25, "major third") off a
 * 16sp body base, which is what gives `display` its "hero number" feel
 * for things like the dashboard's portfolio value while keeping `body`
 * at a comfortable reading size. `caption` intentionally matches the
 * size `ContextualCaption` (Blueprint 3B.3) needs — small, legible,
 * medium-weight so it holds attention during its ~2.5s fade window
 * without shouting.
 */
data class TradeTypographyTokens(
    /** Hero numbers — portfolio value, big balance displays. */
    val display: TextStyle,
    /** Screen titles, section headers. */
    val heading: TextStyle,
    /** Sub-headers, card titles, emphasized list rows. */
    val subheading: TextStyle,
    /** Default reading text — the type-scale base. */
    val body: TextStyle,
    /** De-emphasized body text — secondary metadata, helper copy. */
    val bodySmall: TextStyle,
    /** Smallest scale — timestamps, fine print, `ContextualCaption`. */
    val caption: TextStyle,
    /** All-caps-style small label, e.g. section eyebrows, chip text. */
    val label: TextStyle,
)

/**
 * The one `TradeTypographyTokens` instance — see class doc for why
 * there's no dark/light split.
 */
val TradeTypography = TradeTypographyTokens(
    display = TextStyle(fontSize = 38.sp, lineHeight = 44.sp, fontWeight = FontWeight.Bold),
    heading = TextStyle(fontSize = 28.sp, lineHeight = 34.sp, fontWeight = FontWeight.Bold),
    subheading = TextStyle(fontSize = 20.sp, lineHeight = 26.sp, fontWeight = FontWeight.SemiBold),
    body = TextStyle(fontSize = 16.sp, lineHeight = 22.sp, fontWeight = FontWeight.Normal),
    bodySmall = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Normal),
    caption = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium),
    label = TextStyle(fontSize = 11.sp, lineHeight = 14.sp, fontWeight = FontWeight.SemiBold),
)
