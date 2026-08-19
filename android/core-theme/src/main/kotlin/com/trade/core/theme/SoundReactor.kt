package com.trade.core.theme

import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.media.SoundPool
import kotlinx.coroutines.flow.collect

/**
 * D1/Slice 10 — `SoundReactor` (Blueprint Layer 1C.2). "A non-Composable
 * singleton, mounted once at app root alongside `ThemeReactor`" that
 * subscribes to the **same** [ThemeEventBus] `SharedFlow<ThemeEvent>`
 * `ThemeReactor` (Slice 11) will — Blueprint 1C.2's non-negotiable rule,
 * "no second event bus."
 *
 * Deliberately a plain class, not an `object` like [ThemeEventBus] —
 * unlike the bus, this holds a [Context] and a [SoundPool] that need a
 * lifecycle (created/released), so it can't be a static singleton the
 * way a stateless event pipe can. The caller (`MainActivity`'s
 * `TradeAppShellHost`, as of this slice) owns one instance via
 * `remember { SoundReactor(context) }`, starts [start] from a
 * `LaunchedEffect`, and calls [release] from a `DisposableEffect`.
 *
 * **Silences respected, in the order Blueprint 1C.2 specifies:**
 * 1. Device silent/DND — checked via [AudioManager]'s ringer mode *and*
 *    [NotificationManager]'s interruption filter (ringer mode alone
 *    misses DND states that don't also mute the ringer). **Never
 *    overridden** — no bypass path exists in this class at all.
 * 2. The in-app "sound on/off" toggle ([soundEnabled]) — defaults `true`
 *    since Topic 8's real Notification Settings screen (D8.S03, Section
 *    3D) doesn't exist yet to wire a real value in. Exposed as a plain
 *    `var` so that slice can flip it later without touching this file
 *    beyond a doc update — deliberately not wired to any UI in this
 *    slice, that would be guessing ahead of D8.S03's own scope.
 *
 * **Playback mechanism, and a flagged deviation from Blueprint 1C.2's
 * literal wording**: the blueprint specifies `SoundPool` +
 * `AudioAttributes` throughout, and this class does build and hold a
 * [SoundPool] for that path — but every [SoundTokens] entry currently
 * has a `null` `soundResId` (no bundled `res/raw` audio assets exist in
 * this sandbox session; see `SoundTokens.kt`'s own doc). So for every
 * event today, [playFor] falls through to `RingtoneManager`'s device
 * default notification sound via a short-lived [MediaPlayer] instead —
 * Blueprint 1C.1's explicit D-phase allowance ("short placeholder tones
 * **or system defaults**"), just resolved through a different native API
 * than the one named for the *final* mechanism. Once any [SoundTokens]
 * entry gets a real `soundResId` (a later D-phase session, or R-phase),
 * [playFor] automatically prefers [SoundPool] for that entry — no other
 * code path needs to change. Flagged in `HANDOVER.md` Section 6.
 */
class SoundReactor(
    private val context: Context,
    var soundEnabled: Boolean = true,
) {

    private val soundPool = SoundPool.Builder()
        .setMaxStreams(4)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build(),
        )
        .build()

    /** `res/raw` id -> loaded `SoundPool` sound id, so repeat cues don't reload from disk every time. */
    private val loadedSoundIds = mutableMapOf<Int, Int>()

    /**
     * Suspends forever, collecting [ThemeEventBus.events] and playing the
     * matching [SoundTokens] cue for each one. Call from a `LaunchedEffect`
     * (or any coroutine scope that's cancelled when the app root leaves
     * composition) — cancellation stops collection automatically, but does
     * **not** release the [SoundPool]; call [release] separately.
     */
    suspend fun start() {
        ThemeEventBus.events.collect { event -> playFor(event) }
    }

    private fun playFor(event: ThemeEvent) {
        if (!soundEnabled) return
        if (isSilencedByDeviceSettings()) return

        val resId = SoundTokens.cueFor(event).soundResId
        if (resId != null) {
            playViaSoundPool(resId)
        } else {
            playSystemDefault()
        }
    }

    private fun isSilencedByDeviceSettings(): Boolean {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
        if (audioManager != null && audioManager.ringerMode != AudioManager.RINGER_MODE_NORMAL) {
            return true
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        if (notificationManager != null &&
            notificationManager.currentInterruptionFilter != NotificationManager.INTERRUPTION_FILTER_ALL
        ) {
            return true
        }
        return false
    }

    private fun playViaSoundPool(resId: Int) {
        val soundId = loadedSoundIds.getOrPut(resId) { soundPool.load(context, resId, 1) }
        soundPool.play(soundId, 1f, 1f, /* priority = */ 1, /* loop = */ 0, /* rate = */ 1f)
    }

    /**
     * D-phase fallback for every cue today (see class doc) — plays the
     * device's default notification sound via a one-shot [MediaPlayer].
     * Wrapped defensively: a failed UI sound cue (missing ringtone,
     * `MediaPlayer` error) must never crash or interrupt the rest of the
     * app, so any exception here is swallowed after being silently
     * dropped rather than surfaced as an [ThemeEvent.ErrorOccurred] loop.
     */
    private fun playSystemDefault() {
        try {
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) ?: return
            val player = MediaPlayer.create(context, uri) ?: return
            player.setOnCompletionListener { it.release() }
            player.start()
        } catch (_: Exception) {
            // Best-effort UI cue — see doc above.
        }
    }

    /** Releases the [SoundPool]. Call once, when the app root leaves composition (`DisposableEffect`'s `onDispose`). */
    fun release() {
        soundPool.release()
    }
}
