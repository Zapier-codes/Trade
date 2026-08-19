package com.trade.core.theme

/**
 * D1/Slice 10 — `SoundTokens` (Blueprint Layer 1C.1). Maps every
 * [ThemeEvent] type to a [SoundCue]. Unlike [TradeColorTokens], there is
 * **one** token set, no dark/light split — Blueprint 1C.1 is explicit
 * that "sound has no dark/light split."
 *
 * `character` is the human-readable description from the Blueprint 1C.1
 * table — kept as data (not just a code comment) because Topic 8's
 * Notification Settings screen (D8.S03, Section 3D) is a plausible future
 * place to surface "what does each alert sound like" copy, and because it
 * doubles as the QA/reviewer-facing description until real audio exists.
 *
 * `soundResId` is **null for every entry in this slice** — no bundled
 * placeholder tone assets exist in this sandbox session (no way to author
 * or commit binary audio here), so [SoundReactor] falls back to the
 * device's default notification sound (via `RingtoneManager`) whenever a
 * cue's `soundResId` is null, per Blueprint 1C.1's explicit D-phase
 * allowance ("short placeholder tones **or system defaults**"). This
 * means, as of this slice, every event currently plays the *same* system
 * sound — only [character] currently differentiates them, and only in
 * text/QA form, not audibly yet. Flagged in `HANDOVER.md` Section 6:
 * whoever adds real (or even placeholder) `res/raw` audio files should
 * set the matching `soundResId` here — no other file needs to change.
 *
 * **Non-negotiable per Section 3D**: every future [ThemeEvent] added by
 * a later phase must get a [SoundCue] entry here in the same patch that
 * adds the event — the same rule Blueprint 3B.3 already sets for
 * `ThemeReactor`'s visual side.
 */
object SoundTokens {

    /**
     * @param character Human-readable sound description (Blueprint 1C.1 table wording).
     * @param soundResId `R.raw.*` resource id for the actual short audio clip, once one
     *   exists. Null means "no bundled asset yet — [SoundReactor] uses the system default."
     */
    data class SoundCue(
        val character: String,
        val soundResId: Int? = null,
    )

    val tradeExecutedBuy = SoundCue(character = "Short rising two-note chime")
    val tradeExecutedSell = SoundCue(character = "Short falling two-note chime")
    val depositConfirmed = SoundCue(character = "Warm ascending arpeggio (pairs with the gold sweep visual)")
    val aiSignalFired = SoundCue(character = "Soft synth blip, pitch/brightness scales with confidence")
    val withdrawalBroadcast = SoundCue(character = "Low soft \"sent\" whoosh")
    val errorOccurred = SoundCue(character = "Single muted low tone, deliberately not alarming")
    val agentStatusChange = SoundCue(character = "Very quiet ambient tick, easy to miss on purpose")

    /** Resolves the matching [SoundCue] for any [ThemeEvent] — the lookup [SoundReactor] uses on every event. */
    fun cueFor(event: ThemeEvent): SoundCue = when (event) {
        is ThemeEvent.TradeExecuted -> when (event.side) {
            TradeSide.Buy -> tradeExecutedBuy
            TradeSide.Sell -> tradeExecutedSell
        }
        ThemeEvent.DepositConfirmed -> depositConfirmed
        is ThemeEvent.AISignalFired -> aiSignalFired
        ThemeEvent.WithdrawalBroadcast -> withdrawalBroadcast
        ThemeEvent.ErrorOccurred -> errorOccurred
        is ThemeEvent.AgentStatusChange -> agentStatusChange
    }
}
