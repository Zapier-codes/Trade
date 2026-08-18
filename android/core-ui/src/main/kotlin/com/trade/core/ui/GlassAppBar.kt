package com.trade.core.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.trade.core.theme.LocalTradeTheme

/**
 * D1/Slice 8 — `GlassAppBar` (Blueprint 3B.2), the top-bar glass
 * primitive. Built on [GlassSurface] (Slice 7) rather than Material3's
 * `TopAppBar` — the navigation module's `RouteDirectoryScreen` /
 * `EmptyRouteScreen` use plain Material3 `TopAppBar` deliberately (see
 * their own doc comments: "not the real design system"); feature
 * screens from D2 onward should reach for this instead as their real
 * content replaces those placeholders.
 *
 * A fixed 64dp height is used — no `heights.*`/`sizes.*` token group
 * exists yet (same gap as the missing `radius.*` group flagged in
 * Slice 7's handoff), so this is a plain literal for now rather than a
 * guessed-at token reference. Flagged in `HANDOVER.md` Section 6 if a
 * later slice wants to formalize a sizing token group.
 *
 * `title`/`navigationIcon`/`actions` are plain composable slots, same
 * shape as `GlassCard`'s content slot (Slice 7) — this primitive only
 * positions them and applies the glass surface treatment; callers are
 * expected to style their own `Text`/`IconButton` content using
 * `LocalTradeTheme.current` themselves.
 *
 * @param navigationIcon Leading slot (typically a back/menu `IconButton`).
 *   Empty by default for top-level screens that don't need one.
 * @param actions Trailing slot, e.g. one or more `IconButton`s.
 */
@Composable
fun GlassAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    val theme = LocalTradeTheme.current

    GlassSurface(modifier = modifier.fillMaxWidth(), shape = shape) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = theme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            navigationIcon()
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = theme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                title()
            }
            Row(verticalAlignment = Alignment.CenterVertically, content = actions)
        }
    }
}
