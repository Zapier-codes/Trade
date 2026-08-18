package com.trade.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.window.DialogProperties
import com.trade.core.theme.LocalTradeTheme

/**
 * D1/Slice 9 — `GlassDialog` (Blueprint 3B.2), the dialog glass
 * primitive.
 *
 * Built on Material3's `BasicAlertDialog` rather than `AlertDialog` —
 * `BasicAlertDialog` gives an unstyled container (just the dialog
 * window/scrim/focus/dismiss behavior) with no baked-in Material
 * background, title/button slots, or padding to fight against, so the
 * glass surface treatment is the *only* visible styling, same approach
 * `GlassBottomSheet` (Slice 8) took with `ModalBottomSheet`.
 *
 * `content` is a single free-form slot rather than separate
 * title/body/confirm-button parameters — callers compose their own
 * `Text`/`GlassButton` (this slice) layout inside, since dialogs in
 * this app range from a simple confirm to richer content (e.g. a fee
 * breakdown per Blueprint Section 10.1) that a fixed title/body/actions
 * shape wouldn't fit well.
 */
@Composable
fun GlassDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    properties: DialogProperties = DialogProperties(),
    content: @Composable ColumnScope.() -> Unit,
) {
    val theme = LocalTradeTheme.current

    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        properties = properties,
    ) {
        GlassSurface(modifier = Modifier.fillMaxWidth(), shape = shape) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(theme.spacing.lg),
                content = content,
            )
        }
    }
}
