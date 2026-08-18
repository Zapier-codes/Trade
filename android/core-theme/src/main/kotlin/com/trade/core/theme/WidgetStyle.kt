package com.trade.core.theme

/**
 * D1/Slice 9b — Widget Style variants (Blueprint 3B.1, "Widget Style
 * variants" subsection, added post-D1.S09 — see `HANDOVER.md` Section 3E).
 *
 * Glass ([Glass]) is the **default** and the only style every current and
 * future glass primitive is guaranteed to fully support at any given phase
 * boundary (Section 3E, last bullet). The other five are additive, reskin
 * targets for [com.trade.core.ui.GlassSurface]/[com.trade.core.ui.GlassCard]
 * as of this slice — the remaining five primitives (`GlassAppBar`,
 * `GlassBottomSheet`, `GlassButton`, `GlassDialog`, `GlassTooltip`) keep
 * rendering Glass-only regardless of [TradeThemeInstance.style] until a
 * later `D1.S9c`/`S9d`/... slice (or D10's polish pass) picks them up —
 * see Section 3E for the phased rollout plan.
 *
 * Each style still gets its own dark + light expression per the Dark/Light
 * Parity Rule (3B.4) — this enum only names the six styles, it does not
 * carry per-style tokens. Full dedicated 12-set token treatment (six
 * styles × two modes) is the long-term goal (Section 3E); this slice's
 * reskin derives each style's look from the existing [TradeColorTokens]
 * fields plus per-style structural treatment (fill/border/shadow shape),
 * not new token files — see `GlassSurface.kt` for the derivation and
 * `HANDOVER.md` Section 6 for why that scope line was drawn here.
 */
enum class WidgetStyle(
    /** Short label for the Appearance screen's picker (Blueprint table's "Style" column). */
    val label: String,
    /** One-line character description, same wording as the Blueprint 3B.1 table. */
    val description: String,
) {
    Glass(
        label = "Glass",
        description = "Translucent, blurred, glow/shadow-reactive. The default.",
    ),
    Neumorphic(
        label = "Neumorphic",
        description = "Soft extruded surfaces — same-color background and card, dual soft shadows, no border.",
    ),
    FlatMaterial(
        label = "Flat / Material",
        description = "Opaque fills, no blur or glow, flat drop-shadow steps only.",
    ),
    Minimal(
        label = "Minimal (High-Contrast)",
        description = "Near-monochrome, thin 1px borders, no shadow/glow/blur — max legibility.",
    ),
    Skeuomorphic(
        label = "Skeuomorphic",
        description = "Realistic material texture cues — subtle gradients and bevels.",
    ),
    RetroNeon(
        label = "Retro / Neon",
        description = "Dark base, saturated neon-outline strokes and glow, CRT/synthwave-inspired.",
    ),
}
