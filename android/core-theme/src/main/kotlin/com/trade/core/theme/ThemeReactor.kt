package com.trade.core.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import kotlinx.coroutines.flow.collectLatest

/**
 * D1/Slice 11 — `TradeAmbientState` + `LocalTradeAmbient` + `ThemeReactor`
 * (Blueprint 3B.3): "A single `ThemeReactor` composable, mounted near the
 * app root, listens and animates the *global* ambient tokens... Screens
 * don't need to know the event happened — they just render whatever the
 * current animated token values are."
 *
 * This slice's scope is **ambient glow color only** — see the Topic 1
 * phase table: Slice 11 is "Animate `ambientGlow` color per event," Slice
 * 12 is light-source motion, Slice 13 is the contextual-caption slot.
 * [TradeAmbientState] already carries [lightSourceX]/[lightSourceY]/
 * [contextualCaption] fields so the shape matches Blueprint 3B.1's full
 * token list and Slices 12–13 don't need to touch this data class, but
 * this slice deliberately leaves them static (centered / null) rather
 * than guessing ahead at animation behavior that belongs to those slices.
 */
data class TradeAmbientState(
    /** Currently-animated ambient glow color — idle value at rest, event color mid-pulse. */
    val ambientGlow: Color,
    /**
     * Normalized (0..1) ambient light-source position. Static at (0.5, 0.5)
     * — center — until Slice 12 animates it per-event (Blueprint 3B.3's
     * "light source sweeps toward the fill price" etc.).
     */
    val lightSourceX: Float = 0.5f,
    val lightSourceY: Float = 0.5f,
    /**
     * One-line event microcopy (e.g. "Filled at $67,450 — nice entry.").
     * Always `null` until Slice 13 builds the `ContextualCaption` slot and
     * per-event copy — this field exists now so that slice only adds a
     * producer, not a new field threaded through every consumer.
     */
    val contextualCaption: String? = null,
)

/**
 * Falls back to `TradeThemeDark`'s idle glow — same "don't crash an
 * isolated preview, just give it something reasonable" reasoning
 * `LocalTradeTheme`'s own default uses (see that file's doc comment).
 * A screen that reads this outside a mounted [ThemeReactor] gets a
 * static idle color and never animates, which is a reasonable fallback,
 * not a silent bug — [ThemeReactor] is meant to be mounted exactly once,
 * near the app root, same as [TradeTheme].
 */
val LocalTradeAmbient = staticCompositionLocalOf {
    TradeAmbientState(ambientGlow = TradeThemeDark.ambientGlow)
}

/**
 * Mount once near the app root, **inside** `TradeTheme { }` (needs
 * [LocalTradeTheme.current] to know the active theme's idle
 * [TradeColorTokens.ambientGlow] and, per event, its semantic colors) and
 * alongside [SoundReactor] (Blueprint 1C.2 — both subscribe to the same
 * [ThemeEventBus], neither owns the other).
 *
 * Behavior per event, matching Blueprint 3B.3's table at the *color*
 * level (light-source motion and caption are Slices 12/13, not this
 * one): attack — animate from the current value to the event's target
 * color (fast, [FastOutSlowInEasing]); release — animate back to the
 * *current* idle color (slower, [LinearOutSlowInEasing]), so a fill, a
 * deposit, an error, etc. all read as a distinct pulse rather than a
 * held color change. "Current idle color" (not the idle color captured
 * when the effect started) is read via [rememberUpdatedState] so a
 * dark/light toggle mid-session doesn't leave the release phase settling
 * on a stale value.
 *
 * Uses [collectLatest], not `collect` — Blueprint 3B.3 describes this as
 * a single continuously-updated ambient value, not a queue of full
 * pulse-cycles to play back to back. A new event preempts whatever pulse
 * is currently in flight (`Animatable.animateTo` cancels its own prior
 * in-flight animation when called again — standard, documented
 * behavior, not a workaround), so a burst of events (e.g. `AgentStatusChange`
 * firing right before a `TradeExecuted`) never backs up into a visible
 * lag. Flagged in `HANDOVER.md` Section 6 as a real tuning question for a
 * later session once actual event frequency is known, not a limitation
 * to silently work around here.
 *
 * Separately, [LaunchedEffect] keyed on the idle color itself re-settles
 * the glow whenever dark/light mode changes with no event in flight — a
 * mode toggle is a deliberate, infrequent user action (Settings), so a
 * short transition is desired, not just tolerated. Because both this and
 * the per-event effect call `animateTo` on the same [Animatable], the
 * more-recent call always wins for the same reason described above.
 *
 * No `durationFast/Med/Slow` motion token group exists yet (Blueprint
 * 3B.1 lists one; `GlassSurface`'s Slice 7 doc already flagged the
 * matching gap for `radius.*`/`blurRadius` — same open item, not
 * re-solved here). Attack/release durations below are local constants,
 * not tokens — move them into a real `MotionTokens` group in whichever
 * later slice finally builds one, flagged in `HANDOVER.md` Section 6.
 */
@Composable
fun ThemeReactor(content: @Composable () -> Unit) {
    val theme = LocalTradeTheme.current
    val idleGlow = theme.colors.ambientGlow
    val currentIdleGlow by rememberUpdatedState(idleGlow)
    val currentColors by rememberUpdatedState(theme.colors)

    val glow = remember { Animatable(idleGlow) }
    // Created exactly once, for ThemeReactor's whole lifetime — deliberately
    // NOT `remember(theme)` or `remember(idleGlow)`. Keying the Animatable's
    // creation on either would *recreate* it (a synchronous jump to the new
    // idle color, no animation) every time dark/light mode or WidgetStyle
    // changes, which would make the "smooth re-settle" LaunchedEffect below
    // pointless — it would always find an Animatable that already landed on
    // the new idle color with nothing left to animate from. Its *initial*
    // value only matters for the very first composition; every later idle-
    // color change is handled by that LaunchedEffect instead.

    LaunchedEffect(Unit) {
        ThemeEventBus.events.collectLatest { event ->
            val target = eventGlowSpec(event, currentColors)
            glow.animateTo(
                targetValue = target.color,
                animationSpec = tween(durationMillis = target.attackDurationMs, easing = FastOutSlowInEasing),
            )
            glow.animateTo(
                targetValue = currentIdleGlow,
                animationSpec = tween(durationMillis = target.releaseDurationMs, easing = LinearOutSlowInEasing),
            )
        }
    }

    LaunchedEffect(idleGlow) {
        // Re-settle on a mode change (Settings toggle) even with no event
        // in flight. See class doc for why calling animateTo again here is
        // safe against the per-event effect above.
        glow.animateTo(idleGlow, tween(durationMillis = IdleRetuneDurationMs))
    }

    val ambientState = TradeAmbientState(ambientGlow = glow.value)
    CompositionLocalProvider(LocalTradeAmbient provides ambientState, content = content)
}

/** One event's animated-glow target + how fast it attacks/releases. */
private data class EventGlowSpec(
    val color: Color,
    val attackDurationMs: Int,
    val releaseDurationMs: Int,
)

private const val DefaultAttackDurationMs = 220
private const val DefaultReleaseDurationMs = 900
private const val IdleRetuneDurationMs = 300

/**
 * Maps each [ThemeEvent] to the color/timing its ambient-glow pulse
 * targets, per Blueprint 3B.3's table (dark-mode column — light mode's
 * "shadow, not glow" expression, Blueprint 3B.4, is [TradeColorTokens]'
 * own doc's job: light theme's `ambientGlow` value is already a soft
 * shadow tone rather than a bright accent, so this function doesn't
 * branch on dark/light itself — it just targets the *current* theme's
 * semantic color, same "never branch on mode directly" rule every other
 * primitive in this codebase follows).
 *
 * Judgment calls made here, flagged rather than silently assumed (same
 * as `GlassSurface`'s `RetroNeon` doc comment sets precedent for):
 * - `AISignalFired.confidence` scales *how far* the glow travels toward
 *   `accentSignal` (a low-confidence signal barely nudges the color) via
 *   [lerp] between the idle color and `accentSignal`, rather than
 *   varying alpha/opacity — `Color` here has no independent opacity
 *   concept once it's the *target* of a `Color`-typed `Animatable`.
 * - `WithdrawalBroadcast` has no dedicated semantic token in
 *   `TradeColorTokens` (unlike `positive`/`negative`/`depositGold`/
 *   `error`) — uses `accentPrimary`, the closest fit for a neutral
 *   "action confirmed" ripple that isn't itself a gain/loss/deposit/
 *   error color. Revisit if a later session adds a dedicated token.
 * - `AgentStatusChange` uses a small `lerp` toward `accentSignal` (not
 *   the full color) and shorter attack/release than the default, mirroring
 *   `SoundTokens`' description of the same event's sound cue as "very
 *   quiet... easy to miss on purpose" (Blueprint 1C.1) — the visual and
 *   audio characters should match.
 */
private fun eventGlowSpec(event: ThemeEvent, colors: TradeColorTokens): EventGlowSpec = when (event) {
    is ThemeEvent.TradeExecuted -> when (event.side) {
        TradeSide.Buy -> EventGlowSpec(colors.positive, DefaultAttackDurationMs, DefaultReleaseDurationMs)
        TradeSide.Sell -> EventGlowSpec(colors.negative, DefaultAttackDurationMs, DefaultReleaseDurationMs)
    }
    ThemeEvent.DepositConfirmed ->
        EventGlowSpec(colors.depositGold, DefaultAttackDurationMs, DefaultReleaseDurationMs)
    is ThemeEvent.AISignalFired -> {
        val confidence = event.confidence.coerceIn(0f, 1f)
        EventGlowSpec(
            color = lerp(colors.ambientGlow, colors.accentSignal, confidence),
            attackDurationMs = DefaultAttackDurationMs,
            releaseDurationMs = DefaultReleaseDurationMs,
        )
    }
    ThemeEvent.WithdrawalBroadcast ->
        EventGlowSpec(colors.accentPrimary, DefaultAttackDurationMs, DefaultReleaseDurationMs)
    ThemeEvent.ErrorOccurred ->
        EventGlowSpec(colors.error, DefaultAttackDurationMs, DefaultReleaseDurationMs)
    is ThemeEvent.AgentStatusChange ->
        EventGlowSpec(
            color = lerp(colors.ambientGlow, colors.accentSignal, 0.25f),
            attackDurationMs = 400,
            releaseDurationMs = 400,
        )
}
