package com.trade.app.pawns

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Row
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Plain-language bandwidth-sharing disclosure + opt-in, per Blueprint 10.5.
 *
 * D-phase (Slice 1b): plain Material3 components — this gets reskinned onto
 * the Glass primitives once they exist (Slices 7-9), not before, since this
 * modal needs to exist and be reviewable ahead of the theming engine.
 *
 * Four tabs are non-negotiable per the addendum: General, Privacy,
 * Data Protection, Data Sharing. All four must be accepted before
 * [onConsentComplete] fires — missing a tab or a toggle blocks this
 * slice's acceptance, not a follow-up item.
 */
@Composable
fun ConsentModal(
    onConsentComplete: (PawnsManager.ConsentState) -> Unit,
    onDecline: () -> Unit,
) {
    val tabs = listOf("General", "Privacy", "Data Protection", "Data Sharing")
    var selectedTab by remember { mutableStateOf(0) }

    var generalAccepted by remember { mutableStateOf(false) }
    var privacyAccepted by remember { mutableStateOf(false) }
    var dataProtectionAccepted by remember { mutableStateOf(false) }
    var dataSharingAccepted by remember { mutableStateOf(false) }

    val state = PawnsManager.ConsentState(
        generalAccepted = generalAccepted,
        privacyAccepted = privacyAccepted,
        dataProtectionAccepted = dataProtectionAccepted,
        dataSharingAccepted = dataSharingAccepted,
    )

    Column(modifier = Modifier.padding(16.dp)) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) },
                )
            }
        }

        // Placeholder plain-language copy per tab. Real copywriting pass is
        // out of Slice 1b's scope (see D2/D8 copy-focused slices for the
        // project's copy-pass pattern) — this text just needs to be honest
        // and clear enough to review the consent UX shape.
        val bodyText = when (selectedTab) {
            0 -> "TRADE uses idle device bandwidth via the Pawns SDK to support network operations. This is optional and can be turned off anytime in Privacy settings."
            1 -> "No personal trading data is shared through bandwidth sharing. Only network bandwidth is used, not your account activity."
            2 -> "Your device identity is not linked to bandwidth-sharing data at the SDK level. See docs/TRADE_BLUEPRINT_v2.md Section 10.5 for the full data-handling spec."
            else -> "You can revoke this consent at any time from Privacy settings (Slice 9.17), which immediately stops sharing."
        }
        Text(text = bodyText, modifier = Modifier.padding(vertical = 12.dp))

        val (checked, onCheckedChange) = when (selectedTab) {
            0 -> generalAccepted to { v: Boolean -> generalAccepted = v }
            1 -> privacyAccepted to { v: Boolean -> privacyAccepted = v }
            2 -> dataProtectionAccepted to { v: Boolean -> dataProtectionAccepted = v }
            else -> dataSharingAccepted to { v: Boolean -> dataSharingAccepted = v }
        }
        Row {
            Checkbox(checked = checked, onCheckedChange = onCheckedChange)
            Text(text = "I accept the ${tabs[selectedTab]} terms")
        }

        Row(modifier = Modifier.padding(top = 16.dp)) {
            Button(onClick = onDecline) { Text("Decline") }
            Button(
                onClick = { onConsentComplete(state) },
                enabled = state.allAccepted,
            ) { Text("Accept & Continue") }
        }
    }
}
