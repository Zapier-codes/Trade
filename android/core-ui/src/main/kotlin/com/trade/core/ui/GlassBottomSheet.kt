package com.trade.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import com.trade.core.theme.LocalTradeTheme

/**
 * D1/Slice 8 — `GlassBottomSheet` (Blueprint 3B.2). Wraps Material3's
 * `ModalBottomSheet` (already a `core-ui` dependency via `material3`,
 * no new Gradle dependency needed — same reasoning `HANDOVER.md`
 * Section 6 recorded for `GlassTooltip`) rather than reimplementing
 * modal presentation, drag-to-dismiss, and scrim handling from
 * scratch. Everything Material3 draws by default (its own container
 * background, corner shape, drag handle) is switched off so the only
 * thing visible is the glass-styled content, built on [GlassSurface]
 * (Slice 7) — matching `GlassAppBar`'s approach in this same slice of
 * reusing Slice 7 rather than styling each new primitive independently.
 *
 * @param shape Passed straight through to the inner [GlassSurface] —
 *   same "no `radius.*` token group yet" gap noted there and in
 *   `GlassAppBar`; callers pass e.g. a top-rounded `RoundedCornerShape`
 *   explicitly until that token group exists.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlassBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    shape: Shape = RectangleShape,
    content: @Composable ColumnScope.() -> Unit,
) {
    val theme = LocalTradeTheme.current

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        // Material3's default container/shape/scrim are opaque and
        // Material-styled — switched off so only GlassSurface's fill/
        // border/glow-or-shadow treatment (mode-aware, per Slice 7) is
        // visible.
        containerColor = Color.Transparent,
        contentColor = theme.colors.textPrimary,
        dragHandle = null,
        shape = RectangleShape,
    ) {
        GlassSurface(modifier = Modifier.fillMaxWidth(), shape = shape) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(theme.spacing.md),
                content = content,
            )
        }
    }
}
