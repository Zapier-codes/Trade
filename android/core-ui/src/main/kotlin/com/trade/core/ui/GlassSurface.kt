package com.trade.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.trade.core.theme.LocalTradeTheme
import com.trade.core.theme.TradeThemeDark

/**
 * D1/Slice 7 — `GlassSurface`, the base glass primitive (Blueprint
 * 3B.2). Every other glass component (`GlassCard` here, and
 * `GlassAppBar`/`GlassBottomSheet`/`GlassButton`/`GlassDialog` in
 * Slices 8-9) builds on this rather than re-implementing the fill/
 * border/glow treatment per-component.
 *
 * Mode expression comes entirely from [LocalTradeTheme]'s active color
 * tokens — this composable never branches on dark/light itself:
 * - Dark mode: `surfaceGlass` (near-black translucent fill),
 *   `borderGlassStroke` (cool-white hairline), plus a soft ambient-glow
 *   shadow using `ambientGlow` — the static "subtle inner glow" from
 *   3B.2. This is the *idle* glow only; `ThemeReactor` (Slices 10-11)
 *   animates `ambientGlow` at runtime later — this primitive just needs
 *   to already read the token so that wiring is a no-op here.
 * - Light mode: `surfaceGlass` (bright frosted-white fill),
 *   `borderGlassStroke` (warm hairline) — no glow shadow, since 3B.4
 *   ("Dark/Light Parity Rule") calls for shadow depth over glow in
 *   light mode. Light mode's depth comes from [elevationShadow] instead
 *   (a plain neutral drop shadow, not a colored glow).
 *
 * Real background blur (`blurRadius` token, Blueprint 3B.1) is
 * intentionally **not** applied here yet — `Modifier.blur` needs a
 * real content-behind-the-surface to blur, which doesn't exist for a
 * standalone primitive in isolation, and R1's perf-pass note for this
 * slice ("blur cost on real devices") implies the real blur treatment
 * gets tuned against actual screens, not invented speculatively here.
 * Flagged in `HANDOVER.md` Section 6.
 *
 * @param shape Corner shape. No `radius.*` token group exists yet
 *   (not scoped to any slice so far) so callers pass an explicit
 *   [Shape]; default is unrounded. Flagged in `HANDOVER.md` Section 6.
 * @param glowElevation Idle ambient-glow shadow strength in dark mode.
 *   Ignored in light mode (see class doc).
 * @param elevationShadow Plain neutral drop-shadow strength, used in
 *   light mode for depth instead of glow.
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

    val glassModifier = if (isDark) {
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

    Box(modifier = glassModifier) {
        content()
    }
}
