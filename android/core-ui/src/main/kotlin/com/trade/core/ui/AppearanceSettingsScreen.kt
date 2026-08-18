package com.trade.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.trade.core.theme.LocalTradeTheme
import com.trade.core.theme.WidgetStyle

/**
 * D1/Slice 9b — Settings > Appearance screen (Blueprint 3B.1 "Widget
 * Style variants", `HANDOVER.md` Section 3E).
 *
 * Lets the user pick one of the six [WidgetStyle] values. Selection is
 * **in-memory only**, per Section 3E's D1.S9b scope line — no
 * persistence yet, so a process death loses the choice back to
 * [WidgetStyle.Glass]. Real persistence (DataStore) is R-phase scope.
 *
 * Each row renders a **swatch preview** — a small [GlassCard] forced to
 * that row's style regardless of the app's currently-active style, via
 * a local [CompositionLocalProvider] override — so the user can compare
 * all six options side by side before committing, rather than having to
 * select one, back out, and look at the rest of the app to judge it.
 * Only [GlassSurface]/[GlassCard] are reskinned as of this slice (see
 * `GlassSurface.kt`'s class doc), so the swatch is a card, not a full
 * mock screen — an honest preview of exactly what's built so far, not a
 * mockup of primitives that don't exist yet.
 *
 * Deliberately does **not** build on [GlassAppBar]/[GlassButton] for its
 * own chrome (header, back affordance, row selection) — those two
 * primitives are Glass-only until a later slice (Section 3E), and using
 * them here would make this specific screen's own header/back-button
 * look inconsistent with whatever body style the user just picked. This
 * screen instead reads [LocalTradeTheme] tokens directly for its chrome,
 * same spirit as [GlassCard]'s existing plain-`Column` content slot.
 *
 * @param currentStyle the style currently active app-wide (from the
 *   caller's hoisted state — see `MainActivity`'s `TradeAppShellHost`).
 * @param onStyleSelected fired with the tapped row's style; caller is
 *   expected to feed this back into [com.trade.core.theme.TradeTheme]'s
 *   `style` param so the whole app reskins, not just this screen.
 */
@Composable
fun AppearanceSettingsScreen(
    currentStyle: WidgetStyle,
    onStyleSelected: (WidgetStyle) -> Unit,
    onBack: () -> Unit,
) {
    val theme = LocalTradeTheme.current
    val colors = theme.colors

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(theme.spacing.md),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .clickable(onClick = onBack)
                        .padding(theme.spacing.sm),
                ) {
                    androidx.compose.material3.Text(
                        text = "←",
                        style = theme.typography.heading,
                        color = colors.textPrimary,
                    )
                }
                Spacer(modifier = Modifier.size(theme.spacing.sm))
                androidx.compose.material3.Text(
                    text = "Appearance",
                    style = theme.typography.heading,
                    color = colors.textPrimary,
                )
            }
            androidx.compose.material3.Text(
                text = "Choose how TRADE looks. Glass is the default — this " +
                    "choice isn't saved yet (in-memory only for this slice).",
                style = theme.typography.bodySmall,
                color = colors.textSecondary,
                modifier = Modifier.padding(horizontal = theme.spacing.md),
            )
            Spacer(modifier = Modifier.size(theme.spacing.md))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = theme.spacing.md,
                    vertical = theme.spacing.sm,
                ),
                verticalArrangement = Arrangement.spacedBy(theme.spacing.sm),
            ) {
                items(WidgetStyle.entries) { style ->
                    WidgetStyleRow(
                        style = style,
                        selected = style == currentStyle,
                        onSelected = { onStyleSelected(style) },
                    )
                }
            }
        }
    }
}

/**
 * One selectable row: swatch preview (forced to [style]) + label/
 * description read from [WidgetStyle] itself, + a selection indicator.
 * The whole row is a [GlassCard] rendered in the app's *actual* active
 * style (not [style]) so the list itself looks like part of the app;
 * only the small swatch inside is forced.
 */
@Composable
private fun WidgetStyleRow(
    style: WidgetStyle,
    selected: Boolean,
    onSelected: () -> Unit,
) {
    val theme = LocalTradeTheme.current
    val colors = theme.colors

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelected),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(theme.spacing.md),
        ) {
            // Forced-style swatch — see class doc. Only the fill/border/
            // shadow inside this override reflects `style`; everything
            // else on this screen still reflects the real active style.
            CompositionLocalProvider(LocalTradeTheme provides theme.copy(style = style)) {
                GlassCard(
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Box(modifier = Modifier.fillMaxSize())
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                androidx.compose.material3.Text(
                    text = style.label,
                    style = theme.typography.subheading,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                    color = colors.textPrimary,
                )
                androidx.compose.material3.Text(
                    text = style.description,
                    style = theme.typography.caption,
                    color = colors.textSecondary,
                )
            }

            // Selection indicator — a filled dot in accentPrimary when
            // selected, an outline-only ring otherwise. No radio-button
            // token exists yet, so this is drawn with a plain Box rather
            // than reaching for a Material3 RadioButton (which would
            // never itself reskin per-style, unlike everything else on
            // this screen).
            Box(
                modifier = if (selected) {
                    Modifier
                        .size(20.dp)
                        .background(
                            color = colors.accentPrimary,
                            shape = androidx.compose.foundation.shape.CircleShape,
                        )
                } else {
                    Modifier
                        .size(20.dp)
                        .border(
                            width = 1.5.dp,
                            color = colors.textTertiary,
                            shape = androidx.compose.foundation.shape.CircleShape,
                        )
                },
            )
        }
    }
}
