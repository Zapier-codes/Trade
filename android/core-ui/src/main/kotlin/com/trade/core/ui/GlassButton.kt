package com.trade.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.trade.core.theme.LocalTradeTheme

/**
 * D1/Slice 9 — `GlassButton` (Blueprint 3B.2), the button glass
 * primitive.
 *
 * Two variants, since a screen needs both a strong CTA and a quieter
 * secondary action, and "everything is glass" would flatten that
 * hierarchy:
 * - [GlassButtonVariant.Filled]: solid `accentPrimary` fill, `onAccent`
 *   content color — the CTA case (Blueprint's "primary brand accent
 *   (CTAs, active states)" token description).
 * - [GlassButtonVariant.Glass]: the actual glass treatment (built on
 *   [GlassSurface]) — for secondary/tertiary actions that shouldn't
 *   compete with a screen's primary CTA.
 *
 * `enabled = false` dims via `textDisabled`'s alpha rather than a
 * separate disabled-token set — no such tokens exist yet and adding
 * one wasn't asked for by this slice's scope.
 */
enum class GlassButtonVariant { Filled, Glass }

@Composable
fun GlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: GlassButtonVariant = GlassButtonVariant.Filled,
    enabled: Boolean = true,
    shape: Shape = RectangleShape,
    contentPadding: PaddingValues? = null,
    content: @Composable () -> Unit,
) {
    val theme = LocalTradeTheme.current
    val colors = theme.colors
    val padding = contentPadding ?: PaddingValues(
        horizontal = theme.spacing.lg,
        vertical = theme.spacing.sm,
    )
    val interactionSource = remember { MutableInteractionSource() }

    when (variant) {
        GlassButtonVariant.Filled -> {
            Box(
                modifier = modifier
                    .clip(shape)
                    .background(if (enabled) colors.accentPrimary else colors.textDisabled)
                    .clickable(
                        enabled = enabled,
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick,
                    )
                    .padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                content()
            }
        }

        GlassButtonVariant.Glass -> {
            GlassSurface(
                modifier = modifier
                    .clickable(
                        enabled = enabled,
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick,
                    ),
                shape = shape,
            ) {
                Box(modifier = Modifier.padding(padding), contentAlignment = Alignment.Center) {
                    content()
                }
            }
        }
    }
}
