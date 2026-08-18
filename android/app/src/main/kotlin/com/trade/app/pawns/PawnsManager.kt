package com.trade.app.pawns

import android.content.Context

/**
 * Stub for the Pawns bandwidth-sharing SDK integration (Blueprint 10.5).
 *
 * D-phase (this file, Slice 1b): no real `.aar`, no real API key — every
 * call here is a no-op or fake in-memory state, so the consent UX
 * ([ConsentModal]) can be built and reviewed before any real bandwidth
 * sharing exists.
 *
 * R-phase: wire the real SDK + TRADE's own API key (build-time secret,
 * never hardcoded — see Blueprint 10.5), and make [isConsentGranted] read
 * from real persisted consent state instead of the in-memory fake below.
 */
class PawnsManager(private val context: Context) {

    // Fake in-memory consent record. Real persistence (e.g. EncryptedSharedPreferences
    // or DataStore) is R-phase scope.
    private var fakeConsentGranted: Boolean = false

    /** Fake per-tab consent record shown in [ConsentModal]. R-phase persists this for real. */
    data class ConsentState(
        val generalAccepted: Boolean = false,
        val privacyAccepted: Boolean = false,
        val dataProtectionAccepted: Boolean = false,
        val dataSharingAccepted: Boolean = false,
    ) {
        val allAccepted: Boolean
            get() = generalAccepted && privacyAccepted && dataProtectionAccepted && dataSharingAccepted
    }

    fun isConsentGranted(): Boolean = fakeConsentGranted

    /** Called when the user completes [ConsentModal] with all tabs accepted. */
    fun recordConsent(state: ConsentState) {
        fakeConsentGranted = state.allAccepted
        // R-phase: persist this, then initialize the real SDK with the API key.
    }

    fun revokeConsent() {
        fakeConsentGranted = false
        // R-phase: persist revocation, then stop the real SDK's sharing session.
    }

    /**
     * R-phase: starts real bandwidth sharing if (and only if) consent is on
     * record. D-phase: intentionally does nothing — there is no real SDK
     * session to start.
     */
    fun resumeSharingIfConsented() {
        if (!isConsentGranted()) return
        // No-op in D-phase. R-phase calls into the real Pawns SDK here.
    }
}
