package com.trade.core.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.trade.core.theme.LocalTradeTheme

/**
 * D1/Slice 9 — `GlassTooltip` (Blueprint 3B.2, added post-D1.S04; see
 * `HANDOVER.md` Section 3D for the addendum that folded this into this
 * slice). Contextual help/info overlay, anchored to the element it
 * explains — an info-icon tap or long-press target wraps its anchor
 * content in this composable.
 *
 * Built on Material3's `TooltipBox` + `RichTooltip` — **no new Gradle
 * dependency**, `material3` is already a dependency of `core-ui` (see
 * `GlassBottomSheet`/`GlassDialog`, same reasoning). `RichTooltip`
 * (rather than `PlainTooltip`) is used because it supports a `title` +
 * multi-line body, closer to what "contextual help" content needs than
 * a single short string. Reskinned onto glass tokens via `RichTooltip`'s
 * own `colors` parameter (container/content colors) — no extra wrapping
 * `GlassSurface` composition is needed the way `GlassBottomSheet`/
 * `GlassDialog` needed one.
 *
 * Note: Material3's `RichTooltip` does not expose a border-stroke
 * parameter, so the cool-white/warm hairline border every other glass
 * primitive gets from `GlassSurface` is not reproduced here — flagged
 * in `HANDOVER.md` Section 6 as a small, known parity gap rather than
 * silently skipped.
 *
 * Dismisses on outside tap or timeout (Material3's default tooltip
 * dismiss behavior) and never blocks interaction with the anchor
 * content itself — both requirements straight from the Blueprint spec.
 *
 * @param title Optional bold header line, per `RichTooltip`'s title slot.
 * @param text The tooltip's body copy.
 * @param anchorContent The element this tooltip explains — typically an
 *   info `Icon`/`IconButton`, long-pressed or tapped to reveal the tip.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlassTooltip(
    text: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    anchorContent: @Composable () -> Unit,
) {
    val theme = LocalTradeTheme.current
    val colors = theme.colors

    TooltipBox(
        positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
        tooltip = {
            RichTooltip(
                title = title?.let { { Text(it) } },
                colors = TooltipDefaults.richTooltipColors(
                    containerColor = colors.surfaceGlass,
                    contentColor = colors.textPrimary,
                    titleContentColor = colors.textPrimary,
                ),
                modifier = Modifier.padding(theme.spacing.xs),
            ) {
                Text(text)
            }
        },
        state = rememberTooltipState(),
        modifier = modifier,
    ) {
        anchorContent()
    }
}
