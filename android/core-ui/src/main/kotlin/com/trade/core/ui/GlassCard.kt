package com.trade.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.trade.core.theme.LocalTradeTheme

/**
 * D1/Slice 7 — `GlassCard` (Blueprint 3B.2), the card-shaped glass
 * primitive. Everywhere the phase/scope docs mention a "card" — the
 * dashboard's portfolio-value card, the AI signal card, etc. — those
 * later slices should reach for this rather than a bare `GlassSurface`
 * with hand-rolled padding, so card content spacing stays consistent
 * app-wide.
 *
 * Thin wrapper: all mode expression (fill/border/glow vs. shadow) comes
 * from [GlassSurface] — this only adds the content padding a card
 * needs, sourced from [LocalTradeTheme]'s spacing tokens (Slice 6)
 * rather than a hardcoded value, and lays content out in a `Column`
 * since that's every current card use case (stacked rows of content).
 *
 * @param contentPadding Defaults to `spacing.md` — override for cards
 *   that need denser or looser padding (e.g. a compact list-row card).
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    contentPadding: Dp = LocalTradeTheme.current.spacing.md,
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit,
) {
    GlassSurface(modifier = modifier, shape = shape) {
        Column(modifier = Modifier.padding(contentPadding)) {
            content()
        }
    }
}
