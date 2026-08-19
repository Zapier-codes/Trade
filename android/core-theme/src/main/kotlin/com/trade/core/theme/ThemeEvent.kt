package com.trade.core.theme

/**
 * D1/Slice 10 — the six event types `ThemeEventBus` carries (Blueprint
 * 3B.3's table). Anything that already emits one of these must also have
 * a matching [SoundTokens] entry (Blueprint 3B.3's own non-negotiable
 * rule, extended to sound by Layer 1C / `HANDOVER.md` Section 3D) — the
 * two files are kept in the same `when` shape deliberately so a missing
 * pairing is a compile error in `SoundTokens`, not a silent gap.
 *
 * `ThemeReactor` (Slice 11) is the only planned *visual* consumer of
 * these for now; `SoundReactor` (this slice) is the first *audio*
 * consumer. Both subscribe to the same [ThemeEventBus] — see that file's
 * doc for why there is only one bus.
 *
 * Payload fields are the minimum Section 3B.3's table implies a reactor
 * needs to vary its response: [TradeExecuted.side] (buy vs. sell —
 * dark-mode's glow color and sound's rising/falling chime both depend on
 * it), [AISignalFired.confidence] (glow intensity and sound pitch/
 * brightness both scale with it per the table). The other four events
 * carry no payload — `data object`, not `data class` — because nothing
 * in the table varies their response by any additional data.
 */
sealed class ThemeEvent {
    data class TradeExecuted(val side: TradeSide) : ThemeEvent()
    data object DepositConfirmed : ThemeEvent()
    data class AISignalFired(val confidence: Float) : ThemeEvent()
    data object WithdrawalBroadcast : ThemeEvent()
    data object ErrorOccurred : ThemeEvent()
    data class AgentStatusChange(val active: Boolean) : ThemeEvent()
}

/** Which side a `TradeExecuted` event fired for — buy (green/rising) vs. sell (red/falling). */
enum class TradeSide { Buy, Sell }
