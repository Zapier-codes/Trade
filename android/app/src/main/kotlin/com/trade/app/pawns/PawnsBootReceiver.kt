package com.trade.app.pawns

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Resumes Pawns bandwidth sharing on boot — but only if consent is on
 * record (Blueprint 10.5, Section 3C addendum).
 *
 * D-phase (Slice 1b): no-op stub. There is no real SDK session to resume,
 * and [PawnsManager]'s consent state is in-memory only, so it never
 * survives a real reboot anyway — this class exists purely so the
 * manifest shape and the "check consent before resuming" contract are
 * settled now.
 *
 * R-phase: must call [PawnsManager.resumeSharingIfConsented] against the
 * real persisted consent state — never resume sharing unconditionally.
 */
class PawnsBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        // D-phase: intentionally does nothing further.
        // R-phase: PawnsManager(context).resumeSharingIfConsented()
    }
}
