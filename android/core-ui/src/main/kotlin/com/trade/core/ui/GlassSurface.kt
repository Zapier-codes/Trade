package com.trade.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.trade.core.theme.LocalTradeTheme
import com.trade.core.theme.TradeColorTokens
import com.trade.core.theme.TradeThemeDark
import com.trade.core.theme.WidgetStyle

/**
 * D1/Slice 7 — `GlassSurface`, the base glass primitive (Blueprint
 * 3B.2). Every other glass component (`GlassCard` here, and
 * `GlassAppBar`/`GlassBottomSheet`/`GlassButton`/`GlassDialog` in
 * Slices 8-9) builds on this rather than re-implementing the fill/
 * border/glow treatment per-component.
 *
 * **D1/Slice 9b update:** despite the name, this primitive no longer
 * renders Glass unconditionally — it switches on
 * [com.trade.core.theme.TradeThemeInstance.style] (Blueprint 3B.1
 * "Widget Style variants", `HANDOVER.md` Section 3E) and is the
 * **reference implementation** for all six [WidgetStyle] values, per
 * Section 3E's D1.S9b bullet. `GlassCard` (unchanged) inherits every
 * style automatically since it delegates its fill/border/shadow
 * entirely to this function. The other five glass primitives
 * (`GlassAppBar`, `GlassBottomSheet`, `GlassButton`, `GlassDialog`,
 * `GlassTooltip`) are **not** touched by this slice — they keep
 * rendering Glass-only regardless of the active style until a later
 * `D1.S9c`/`S9d`/... slice picks them up (Section 3E).
 *
 * Mode expression (dark vs. light) still comes entirely from
 * [LocalTradeTheme]'s active color tokens for every style — this
 * composable never branches on dark/light with a literal color, only
 * via [TradeColorTokens] fields, same rule as before Slice 9b. Each
 * style still gets an independent dark/light expression per Blueprint
 * 3B.4 ("Dark/Light Parity Rule") — see the private `xxxStyle`
 * functions below for each style's pair.
 *
 * None of the five new styles get their own dedicated 12-set token
 * files yet (Section 3E's long-term goal) — this slice derives each
 * style's fill/border/shadow from the *existing* [TradeColorTokens]
 * fields plus per-style structural treatment instead. Flagged in
 * `HANDOVER.md` Section 6 as a scope call, particularly relevant for
 * [WidgetStyle.RetroNeon], whose light-mode expression sits in genuine
 * tension with 3B.4's "shadow over glow" guidance — see that style's
 * own doc comment below.
 *
 * Real background blur (`blurRadius` token, Blueprint 3B.1) is still
 * intentionally **not** applied anywhere here — same reasoning as
 * Slice 7's original doc: no real content-behind-the-surface to blur
 * for a standalone primitive in isolation. Flagged in `HANDOVER.md`
 * Section 6.
 *
 * @param shape Corner shape. No `radius.*` token group exists yet
 *   (not scoped to any slice so far) so callers pass an explicit
 *   [Shape]; default is unrounded. Flagged in `HANDOVER.md` Section 6.
 * @param glowElevation Idle ambient-glow shadow strength, used by
 *   [WidgetStyle.Glass] (dark) and [WidgetStyle.RetroNeon] (both
 *   modes) — the two styles whose defining trait is a colored glow.
 * @param elevationShadow Plain neutral drop-shadow strength, used by
 *   every style whose depth comes from shadow rather than glow
 *   ([WidgetStyle.Glass] light mode, [WidgetStyle.FlatMaterial],
 *   [WidgetStyle.Skeuomorphic]).
 */
@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    glowElevation: Dp = 12.dp,
    elevationShadow: Dp = 4.dp,
    content: @Composable () -> Unit,
) {
    val theme = LocalTradeTheme.current
    val colors = theme.colors
    val isDark = colors === TradeThemeDark

    val styledModifier = when (theme.style) {
        WidgetStyle.Glass -> glassStyle(modifier, shape, colors, isDark, glowElevation, elevationShadow)
        WidgetStyle.Neumorphic -> neumorphicStyle(modifier, shape, colors, isDark)
        WidgetStyle.FlatMaterial -> flatMaterialStyle(modifier, shape, colors, elevationShadow)
        WidgetStyle.Minimal -> minimalStyle(modifier, shape, colors)
        WidgetStyle.Skeuomorphic -> skeuomorphicStyle(modifier, shape, colors, isDark, elevationShadow)
        WidgetStyle.RetroNeon -> retroNeonStyle(modifier, shape, colors, isDark, glowElevation)
    }

    Box(modifier = styledModifier) {
        content()
    }
}

/**
 * [WidgetStyle.Glass] — unchanged from Slice 7. Dark: translucent fill +
 * cool-white hairline + ambient-glow shadow. Light: translucent fill +
 * warm hairline + plain neutral shadow (3B.4: shadow over glow in light).
 */
private fun glassStyle(
    modifier: Modifier,
    shape: Shape,
    colors: TradeColorTokens,
    isDark: Boolean,
    glowElevation: Dp,
    elevationShadow: Dp,
): Modifier = if (isDark) {
    modifier
        .shadow(
            elevation = glowElevation,
            shape = shape,
            ambientColor = colors.ambientGlow,
            spotColor = colors.ambientGlow,
        )
        .clip(shape)
        .background(colors.surfaceGlass)
        .border(width = 1.dp, color = colors.borderGlassStroke, shape = shape)
} else {
    modifier
        .shadow(
            elevation = elevationShadow,
            shape = shape,
            ambientColor = Color.Black.copy(alpha = 0.12f),
            spotColor = Color.Black.copy(alpha = 0.12f),
        )
        .clip(shape)
        .background(colors.surfaceGlass)
        .border(width = 1.dp, color = colors.borderGlassStroke, shape = shape)
}

/**
 * [WidgetStyle.Neumorphic] — "same-color background and card, depth from
 * dual soft shadows (light+dark), no border stroke" (Blueprint 3B.1
 * table). Fill is [TradeColorTokens.background] itself, not
 * `surfaceGlass` — the defining trait is that the card barely reads as a
 * separate surface, only as a soft extrusion. Compose's `Modifier.shadow`
 * only draws one shadow layer, so the "dual soft shadow" look is
 * approximated by stacking two `.shadow()` calls — one with a dark
 * ambient/spot pair (the conventional "recessed" shadow), one with a
 * light, low-alpha pair standing in for the classic neumorphic
 * counter-highlight. Both modes use the same technique; only the color
 * pairing shifts (dark mode's "light" shadow is a dim cool-white, light
 * mode's is closer to true white) so the extrusion still reads as
 * *soft*, not as a hard bevel — that's [WidgetStyle.Skeuomorphic]'s job.
 */
private fun neumorphicStyle(
    modifier: Modifier,
    shape: Shape,
    colors: TradeColorTokens,
    isDark: Boolean,
): Modifier {
    val highlight = if (isDark) Color(0x1AE8F1FF) else Color(0xCCFFFFFF)
    return modifier
        .shadow(
            elevation = 10.dp,
            shape = shape,
            ambientColor = Color.Black.copy(alpha = if (isDark) 0.55f else 0.18f),
            spotColor = Color.Black.copy(alpha = if (isDark) 0.55f else 0.18f),
        )
        .shadow(
            elevation = 6.dp,
            shape = shape,
            ambientColor = highlight,
            spotColor = highlight,
        )
        .clip(shape)
        .background(colors.background)
}

/**
 * [WidgetStyle.FlatMaterial] — "opaque fills, no blur or glow, elevation
 * via flat drop-shadow steps only — closest to stock Material3." Uses
 * [TradeColorTokens.surface] (the existing opaque-surface token, not
 * `surfaceGlass`) and a single plain neutral shadow, no border, no glow
 * shadow in either mode — the one style whose dark and light expressions
 * are structurally identical, since "flat opaque + step shadow" already
 * satisfies 3B.4 without a mode-specific twist (a literal reading of
 * "closest to stock Material3," which doesn't reinterpret itself per
 * mode either).
 */
private fun flatMaterialStyle(
    modifier: Modifier,
    shape: Shape,
    colors: TradeColorTokens,
    elevationShadow: Dp,
): Modifier = modifier
    .shadow(
        elevation = elevationShadow,
        shape = shape,
        ambientColor = Color.Black.copy(alpha = 0.20f),
        spotColor = Color.Black.copy(alpha = 0.20f),
    )
    .clip(shape)
    .background(colors.surface)

/**
 * [WidgetStyle.Minimal] — "near-monochrome, thin 1px borders, no shadow/
 * glow/blur at all — built for max legibility/accessibility." No
 * `.shadow()` call at all (the style's whole point), opaque
 * [TradeColorTokens.surface] fill, and the highest-contrast hairline
 * available ([TradeColorTokens.textTertiary] rather than the low-alpha
 * `borderGlassStroke`/`divider` tokens other styles use) so the border
 * stays visible without any elevation cue to lean on.
 */
private fun minimalStyle(
    modifier: Modifier,
    shape: Shape,
    colors: TradeColorTokens,
): Modifier = modifier
    .clip(shape)
    .background(colors.surface)
    .border(width = 1.dp, color = colors.textTertiary, shape = shape)

/**
 * [WidgetStyle.Skeuomorphic] — "realistic material texture cues (subtle
 * gradients, bevels) evoking physical surfaces — the most decorative
 * option." Approximates a bevel with a vertical [Brush.verticalGradient]
 * from [TradeColorTokens.surfaceElevated] (lighter, top) to
 * [TradeColorTokens.surface] (base, bottom) — a physical surface catching
 * light from above — plus a plain drop shadow for the "physical object
 * sitting on the background" read. No border: a beveled gradient edge
 * reads as material thickness, a hairline on top of it would look like a
 * sticker outline instead.
 */
private fun skeuomorphicStyle(
    modifier: Modifier,
    shape: Shape,
    colors: TradeColorTokens,
    isDark: Boolean,
    elevationShadow: Dp,
): Modifier = modifier
    .shadow(
        elevation = elevationShadow * 1.5f,
        shape = shape,
        ambientColor = Color.Black.copy(alpha = if (isDark) 0.5f else 0.22f),
        spotColor = Color.Black.copy(alpha = if (isDark) 0.5f else 0.22f),
    )
    .clip(shape)
    .background(
        Brush.verticalGradient(
            colors = listOf(colors.surfaceElevated, colors.surface),
        ),
    )

/**
 * [WidgetStyle.RetroNeon] — "dark base with saturated neon-outline
 * strokes and glow, CRT/synthwave-inspired — the most stylized option."
 * Uses [TradeColorTokens.background] (already near-black in dark mode)
 * as the fill in **both** modes, with [TradeColorTokens.accentSignal] as
 * the neon stroke/glow color — the defining character of this style is
 * "dark base," per the Blueprint table's own wording, which doesn't
 * describe a light variant the way the other five styles implicitly do.
 *
 * **Flagged tension with Blueprint 3B.4** ("Dark/Light Parity Rule":
 * light mode should favor shadow depth over glow, hairline over
 * saturated fill): a literal light-mode reading would mean no glow and
 * no saturated neon stroke at all, which would stop being "Retro/Neon"
 * in any recognizable sense. Judgment call made here rather than
 * guessed silently (per Section 1's non-negotiable rule) — light mode
 * keeps the dark base and neon stroke (so the style stays identifiable
 * and switching light/dark doesn't silently downgrade a user's chosen
 * style to something else), but dials the glow **down** relative to
 * dark mode (lower elevation, lower alpha) so light mode still reads as
 * the *quieter* of the two, which is the spirit of 3B.4 even though the
 * letter (glow → shadow) doesn't cleanly apply to an inherently-glowing
 * style. Revisit if a future session/human review disagrees — noted in
 * `HANDOVER.md` Section 6.
 */
private fun retroNeonStyle(
    modifier: Modifier,
    shape: Shape,
    colors: TradeColorTokens,
    isDark: Boolean,
    glowElevation: Dp,
): Modifier = modifier
    .shadow(
        elevation = if (isDark) glowElevation else glowElevation / 2,
        shape = shape,
        ambientColor = colors.accentSignal.copy(alpha = if (isDark) 0.9f else 0.5f),
        spotColor = colors.accentSignal.copy(alpha = if (isDark) 0.9f else 0.5f),
    )
    .clip(shape)
    .background(colors.background)
    .border(width = 1.dp, color = colors.accentSignal, shape = shape)
