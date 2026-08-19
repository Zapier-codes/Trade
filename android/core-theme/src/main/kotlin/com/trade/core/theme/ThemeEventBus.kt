package com.trade.core.theme

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * D1/Slice 10 — `ThemeEventBus` (Blueprint 3B.3): "a simple
 * `SharedFlow<ThemeEvent>` that any part of the app can emit into."
 *
 * A Kotlin `object` singleton, not an injectable class — per
 * `android/ARCHITECTURE.md`, DI framework choice is explicit R1 scope,
 * and unlike a feature module's own repository (which gets a manual
 * temporary container per module), this needs to be reachable from
 * *anywhere* in the app with no wiring at all, matching the blueprint's
 * "any part of the app can emit into" wording. R1 may wrap this in a
 * real DI-provided instance once that decision lands — flagged in
 * `HANDOVER.md` Section 6 as an R1 consideration, not changed here.
 *
 * `Layer 1C`'s `SoundReactor` (this slice, `SoundReactor.kt`) subscribes
 * to the *same* [events] flow `ThemeReactor` (Slice 11) will — Blueprint
 * 1C.2's non-negotiable rule, "no second event bus."
 *
 * `extraBufferCapacity` is small and non-zero so a burst of events fired
 * in the same frame (unlikely today, plausible once real trade/AI-signal
 * flows exist) doesn't drop events for a slow collector — collectors here
 * are always cheap (animate a few tokens, play a <1s sound), so replay
 * isn't needed and would only risk re-triggering an old animation/sound
 * for a newly-mounted collector.
 */
object ThemeEventBus {

    private val _events = MutableSharedFlow<ThemeEvent>(extraBufferCapacity = 8)

    /** Collect this from `ThemeReactor`/`SoundReactor` — never emit into it directly, use [emit]/[tryEmit]. */
    val events: SharedFlow<ThemeEvent> = _events.asSharedFlow()

    /** Suspending emit — prefer this from a coroutine (ViewModel, use case) when one is already available. */
    suspend fun emit(event: ThemeEvent) {
        _events.emit(event)
    }

    /** Non-suspending emit — for call sites without a coroutine scope handy. Returns false only if the buffer is full. */
    fun tryEmit(event: ThemeEvent): Boolean = _events.tryEmit(event)
}
