# 🚀 "TRADE" — Blueprint v2 (Cinematic Edition)

This is the upgraded blueprint. It carries forward everything from the
original document (feature comparison vs Bybit, 5-layer architecture,
payment integrations, notification system, user journeys, security
architecture, multi-agent AI system, roadmap) and adds the piece that
was missing: a **Dynamic Cinematic Theming Engine** — the thing that
makes the app feel alive, modern, and "cinematic" rather than just
"dark-mode-with-a-toggle."

This file is the **product/design source of truth**. `HANDOVER.md`
(companion file) is the **execution source of truth** — it tells AI
sessions what to build and in what order. Read both.

---

## 1. What's new in v2

The original blueprint had one theming line: "Zero-Click Onboarding /
AI-Powered Real-Time Localization" and nothing about theming beyond a
generic dark UI. v2 adds:

1. **A real theming architecture** — not just a dark/light switch, but
   a token-driven system (Layer 1B below) that every screen consumes.
2. **Dynamic/reactive theming** — the UI visibly *responds* to what the
   user just did. A trade filling, a deposit landing, an AI signal
   firing, an error occurring — each is a "theming event" that nudges
   color temperature, moves an ambient light source, and can swap a
   line of contextual microcopy. This behaves differently depending on
   whether the user is in **dark mode** or **light mode** — same event,
   two different visual languages.
3. **Glassmorphism as a system, not a one-off effect** — reusable glass
   primitives (see Layer 1B) used everywhere, with dark/light-specific
   blur, tint, and border treatments.
4. **A 2-track build model** — everything in this blueprint gets built
   twice: once as a **Dummy/UI-only** pass (Category 1), then once as
   the **Real Implementation** pass (Category 2). See `HANDOVER.md` for
   how that's scheduled across sessions.

Everything below that isn't explicitly about theming is carried over
from the original blueprint, reorganized to match the 10-phase
structure used in `HANDOVER.md`.

---

## 2. Feature Comparison: "TRADE" vs Bybit

| Feature Category | Bybit | "TRADE" | Advantage |
| :--- | :--- | :--- | :--- |
| Trading Instruments | Spot, Perpetuals, Futures, Options | + Real-World Assets (RWAs) | Wider coverage |
| Leverage | Up to 100x | Up to 100x, capped/guarded | Same, safer |
| Order Types | Market, Limit, SL/TP, Trailing, OCO | + AI-optimized Smart Orders | AI-enhanced execution |
| Trading Engine | Centralized, 300k+ TPS | Hybrid: Centralized + DEX aggregation | Censorship-resistant + fast |
| Liquidity | 100M+ users, 1000+ pairs | Aggregated top-10 DEX+CEX | Deep liquidity, no single point of custody |
| Charting | TradingView | TradingView + AI overlays | AI-annotated charts |
| AI | TradeGPT (analysis only) | Multi-agent (analysis + execution + optimization) | Full automation |
| Copy Trading | Standard | AI + Social copy trading | Smarter copying |
| P2P | Global P2P | Global + Zero-Fee P2P | Lower fees |
| Earn | Savings, Staking, Dual Investment | + AI-optimized yield farming | Better yield |
| Security | 2FA, Cold Storage | Hardware-grade + Biometric + Passkeys + MPC | Stronger, simpler |
| Account | Email/Phone + KYC | No-KYC, device identity, passkeys | Max privacy |
| Deposits | Bank, P2P, Crypto, Cards | Korapay + Juciway + native crypto | More on-ramps |
| Notifications | Push/SMS/Email | Novu (push, in-app, AI-contextual) | Smarter alerts |
| Privacy | Standard | Zero-Knowledge Proofs | Max privacy |
| Decentralization | Fully centralized | Hybrid custody | Best of both |
| Open Source | Proprietary | Fully open source | Auditable |
| Theming | Static dark/light | **Dynamic cinematic theming, event-reactive, glass system** | Feels alive |

---

## 3. System Architecture

### Layer 1: Mobile Application Layer (Kotlin + Jetpack Compose)

| Component | Implementation | Notes |
| :--- | :--- | :--- |
| UI Framework | Jetpack Compose | Declarative, animatable |
| Architecture | Clean Architecture + MVVM | Modular, testable |
| State | StateFlow + Compose State | Reactive |
| Navigation | Compose Navigation | Type-safe routes |
| Onboarding | Interactive Compose tutorials | Zero-click, gamified |
| Dashboard | Widget-based, real-time | AI-driven insights |
| Charts | MPAndroidChart + TradingView WebView | AI overlay layer on top |
| Order Entry | Smart order entry | AI-assisted pricing |
| Wallet | Multi-chain, self-custodial | Hardware-grade security |
| Demo/Live | One-click toggle | Shared UI, separate balances |

---

### Layer 1B: Dynamic Cinematic Theming Engine (NEW)

This is the system that makes "TRADE" feel like a modern, cinematic
product rather than a static skinned app. It has three parts.

#### 3B.1 — Design Tokens (the foundation)

A single `ThemeTokens` object (per mode) drives every screen:

| Token Group | Examples |
| :--- | :--- |
| Color | `surface`, `surfaceGlass`, `accentPrimary`, `accentSignal`, `ambientGlow` |
| Elevation/Blur | `blurRadius.sm/md/lg`, `glassOpacity`, `borderGlassStroke` |
| Motion | `durationFast/Med/Slow`, `easingStandard/Emphasized` |
| Typography | display/heading/body/caption scale + weight |
| Radius | `radius.sm/md/lg/pill` |
| Ambient light position | `lightSource.x/y` (normalized 0–1, animatable) |

Two full token sets exist: `TradeThemeDark` and `TradeThemeLight`. Both
consume the *same* token names — screens never branch on
`isDarkMode`, they just read `LocalTradeTheme.current.X`.

**Widget Style variants (NEW, added post-D1.S09 — see `HANDOVER.md`
Section 3E):** the glass look above is the **default** style, not the
only one. A Settings > Appearance screen lets a user switch to one of
five alternates, each still with its own dark + light pair per the
Dark/Light Parity Rule (3B.4) — six styles × two modes = twelve token
sets in total once fully built out. `LocalTradeTheme.current.style`
carries the active choice; screens still never branch on it directly
— only each primitive's own internal styling logic does.

| Style | Character |
| :--- | :--- |
| **Glass** (default) | The system described throughout this section — translucent, blurred, glow/shadow-reactive. |
| **Neumorphic** | Soft extruded surfaces — same-color background and card, depth from dual soft shadows (light+dark), no border stroke. |
| **Flat / Material** | Opaque fills, no blur or glow, elevation via flat drop-shadow steps only — closest to stock Material3. |
| **Minimal (High-Contrast)** | Near-monochrome, thin 1px borders, no shadow/glow/blur at all — built for max legibility/accessibility. |
| **Skeuomorphic** | Realistic material texture cues (subtle gradients, bevels) evoking physical surfaces — the most decorative option. |
| **Retro/Neon** | Dark base with saturated neon-outline strokes and glow, CRT/synthwave-inspired — the most stylized option. |

Full architecture, slice mapping, and phased rollout plan: `HANDOVER.md`
Section 3E.

#### 3B.2 — Glass Component System

Reusable primitives, built once, used everywhere:
`GlassSurface`, `GlassCard`, `GlassAppBar`, `GlassBottomSheet`,
`GlassButton`, `GlassDialog`. Each renders differently per mode:

- **Dark mode glass**: near-black translucent surface, soft cool-white
  border stroke, subtle inner glow, blur pulls in ambient accent color
  from whatever `ambientGlow` currently is.
- **Light mode glass**: bright frosted-white translucent surface, warm
  hairline border, blur softens background color instead of glowing —
  no neon, more "misted glass" than "sci-fi panel."
- **`GlassTooltip` (NEW, added post-D1.S04)**: contextual help/info
  overlay, anchored to the element it explains (info icon tap or
  long-press). Built on Compose Material3's existing `TooltipBox`/
  `PlainTooltip`/`RichTooltip` primitives (already available via the
  `material3` dependency every module already has — no new Gradle
  dependency needed for this one), reskinned with the same glass
  surface/border/blur tokens as the rest of this list rather than
  Material3's default tooltip styling. Dismisses on outside tap or
  timeout; never blocks interaction with the anchor element itself.
  Build-order details in `HANDOVER.md` Section 3D (new addendum).

#### 3B.3 — Theming Events (the "reactive" part)

A `ThemeEventBus` (simple `SharedFlow<ThemeEvent>`) that any part of
the app can emit into. A single `ThemeReactor` composable, mounted near
the app root, listens and animates the *global* ambient tokens
(`ambientGlow` color, `lightSource` position, and an optional one-line
`contextualCaption`) in response. Screens don't need to know the event
happened — they just render whatever the current animated token values
are, because they already read from `LocalTradeTheme.current`.

| Event | Trigger | Dark-mode effect | Light-mode effect |
| :--- | :--- | :--- | :--- |
| `TradeExecuted(side)` | Order fills | Ambient glow pulses green/red from bottom, light source sweeps toward the fill price | Soft green/red flash tint on the nearest glass surface, no glow — a shadow shift instead |
| `DepositConfirmed` | Funds land | Gold ambient sweep top-to-bottom, brief particle shimmer | Warm cream sweep, no particles — a widening soft shadow ring |
| `AISignalFired(confidence)` | Agent emits signal | Light source orbits toward the AI panel, glow intensity scales with confidence | Border of AI panel gains a soft colored hairline scaled by confidence, no glow |
| `WithdrawalBroadcast` | Tx sent | Ambient glow trails outward like a ripple from the withdraw button | A single soft ripple shadow, fast fade |
| `ErrorOccurred` | Any failure | Ambient glow shifts to muted red-orange, light source dips downward ("heavy" feeling) | Border of the affected card gains a thin warm-red hairline, background stays neutral |
| `AgentStatusChange` | Agent goes active/idle | Subtle breathing glow on agent avatar | Subtle breathing *shadow* on agent avatar (no glow in light mode) |

Each event may also supply a **contextual microcopy line** (e.g. "Filled
at $67,450 — nice entry." / "Funds are in. Let's put them to work.")
rendered in a small `ContextualCaption` slot near the dashboard header,
fading in/out with the event's animation window (~2.5s).

**Rule for every phase from here on**: any new screen that shows a
result of a user action (trade, deposit, withdrawal, AI signal, error)
must emit the matching `ThemeEvent` — it is *not* optional polish, it's
part of the phase's acceptance criteria once Layer 1B exists (from
Phase 1 onward).

#### 3B.4 — Dark/Light Parity Rule

Every themed effect must be designed as a **pair** — one dark-mode
expression, one light-mode expression — never "dark mode gets the cool
effect, light mode just turns the lights on." Light mode's version
should favor: shadow depth over glow, hairline color over saturated
fill, soft ripple over particle bursts. This keeps light mode feeling
premium and considered rather than like a fallback.

---

### Layer 1C: Dynamic Notification & Action Sound Engine (NEW)

Added post-D1.S04. The audio counterpart to Layer 1B: every action that
already emits a `ThemeEvent` (Section 3B.3's table — `TradeExecuted`,
`DepositConfirmed`, `AISignalFired`, `WithdrawalBroadcast`,
`ErrorOccurred`, `AgentStatusChange`) also gets a distinct short sound,
so a fill, a deposit landing, and an error don't just *look* different,
they *sound* different too. This reuses Layer 1B's event bus rather
than duplicating it — see 1C.2.

#### 1C.1 — Sound Tokens

A `SoundTokens` object maps each `ThemeEvent` type to a short (<1s)
UI sound cue. Unlike color tokens, sound has no dark/light split — one
token set. Character guide (final assets are an R-phase concern, D-phase
uses short placeholder tones/system defaults, see `HANDOVER.md` Section
3D):

| Event | Sound character |
| :--- | :--- |
| `TradeExecuted(buy)` | Short rising two-note chime |
| `TradeExecuted(sell)` | Short falling two-note chime |
| `DepositConfirmed` | Warm ascending arpeggio (pairs with the "gold sweep" visual) |
| `AISignalFired(confidence)` | Soft synth blip, pitch/brightness scales with confidence |
| `WithdrawalBroadcast` | Low soft "sent" whoosh |
| `ErrorOccurred` | Single muted low tone, deliberately not alarming |
| `AgentStatusChange` | Very quiet ambient tick, easy to miss on purpose (agents change status often) |

Every future `ThemeEvent` added by a later phase must get a `SoundTokens`
entry in the same patch that adds the event — same non-negotiable rule
Section 3B.3 already sets for the visual side.

#### 1C.2 — SoundReactor

A `SoundReactor` (non-Composable singleton, mounted once at app root
alongside `ThemeReactor`) subscribes to the **same** `ThemeEventBus`
`SharedFlow<ThemeEvent>` Layer 1B already defines — no second event bus.
On each event it looks up the matching `SoundTokens` entry and plays it
via `android.media.SoundPool` + `AudioAttributes` (`USAGE_NOTIFICATION_EVENT`
/ `CONTENT_TYPE_SONIFICATION`) — the standard low-latency native API for
short UI sound cues, not a third-party audio library; no new Gradle
dependency is needed for this layer. `SoundReactor` respects, in order:
device silent/DND mode (never overridden), then the in-app "sound on/off"
toggle added to the Notification Settings screen (Topic 8, D8.S03 — see
`HANDOVER.md` Section 3D for exact slice mapping).

#### 1C.3 — Relationship to Push Notification Sounds (Topic 8)

Android `NotificationChannel` sounds (used for actual push notifications,
Topic 8) are a **separate** mechanism from in-app `SoundReactor` cues —
channels play their sound when the app isn't foregrounded, `SoundReactor`
plays when it is. Both should use the *same* `SoundTokens` character
per event type so the experience is consistent whether the app is open
or not, but the plumbing (channel sound URI vs. `SoundPool` playback) is
implemented separately per Topic 8's own D8/R8 slices.

---

### Layer 2: AI & Multi-Agent System (TradingAgents)

Deployed as a Python microservice, exposed via REST + WebSocket.

| Agent | Function |
| :--- | :--- |
| Market Analyst | RSI, MACD, Bollinger, moving averages |
| Sentiment Agent | NLP over news/social |
| Trader Agent | RL-based buy/sell signal generation |
| Portfolio Manager | MPT + genetic allocation |
| Risk Manager | VaR + Monte Carlo |
| Execution Layer | Smart order routing |
| Backtesting Engine | Historical simulation |

Flow: `Android App → REST/WebSocket → TradingAgents Python Service →
LLM Provider → Analysis → Response → Android UI` (and, per Layer 1B,
`→ ThemeEventBus.emit(AISignalFired(...))`).

---

### Layer 3: Security & Identity

| Component | Implementation |
| :--- | :--- |
| Authentication | Android Credential Manager + Passkeys |
| Device Binding | Android Keystore + TEE |
| Key Management | MPC |
| Wallet Security | HSM, EAL 6+ |
| Private Keys | Never leave device |
| Privacy | ZK-SNARKs |
| Anti-Phishing | AI-powered detection |
| Transaction Validation | 2FA + biometric + device binding |
| Recovery | Social recovery + seed phrase |

---

### Layer 4: Backend Services

API Gateway (Ktor/Spring Boot) · Real-time data (WebSocket/SSE) ·
PostgreSQL + Supabase · Redis cache · RabbitMQ/Kafka · LMAX
Disruptor matching engine · DEX liquidity aggregator · AI model serving
(TensorFlow Serving/ONNX) · Novu notifications · ELK + Prometheus ·
Korapay + Juciway payments · IPGeolocation.io · dual demo/live balance
system.

---

### Layer 5: Trading Infrastructure

Spot via DEX aggregation (1inch, Uniswap, PancakeSwap) · Perpetuals via
Hyperliquid/dYdX · Options via Premia/Derive · custom zero-fee P2P
engine · smart-contract copy trading · Aave/Compound earn products ·
cross-chain bridge aggregator · paper-trading demo simulator.

---

## 4. Payment Integration

### Korapay
Card, bank transfer, mobile money. Flow: app → backend creates payment
session → CustomTabs/WebView → callback verified → balance updated →
push via Novu.

### Juciway (Hyperswitch SDK)
Card, bank transfer, crypto, Interac e-Transfer. Flow: app → backend
creates payment intent → native Hyperswitch Payment Sheet → webhook
confirms → balance updated.

Both flows must emit `ThemeEvent.DepositConfirmed` on success and
`ThemeEvent.ErrorOccurred` on failure (Layer 1B).

---

## 5. Notification System (Novu Cloud)

Push (FCM), in-app feed, email (security alerts), real-time alerts.
Triggers: trade execution, AI strategy updates, deposit/withdrawal
confirmations, AI-generated price alerts, security notices, promos.
Each trigger category maps to a `ThemeEvent` where one exists (trade →
`TradeExecuted`, deposit → `DepositConfirmed`, etc.) so the in-app
notification and the ambient theming reaction fire together.

---

## 6. User Journeys

1. **Onboarding (zero-click)**: install → branded welcome → 3–5
   interactive tutorials → minimal ToS → dashboard.
2. **Dashboard**: demo/live toggle, portfolio value + 24h change,
   deposit/withdraw/trade/AI-agent actions, AI allocation chart, agent
   status (with Layer 1B breathing-glow/shadow), live market feed, AI
   market summary, notifications.
3. **Deposit**: choose method (Korapay: card/bank/mobile money;
   Juciway: crypto/Interac) → amount (auto-converted via IP) → pay →
   instant credit → `DepositConfirmed` event.
4. **Manual trading**: pick pair → chart + AI overlay → demo/live →
   place order → `TradeExecuted` event.
5. **AI trading**: pick strategy (Scalp/Momentum/Grid/Arbitrage) →
   demo/live → risk tolerance → activate → multi-agent system trades →
   `AISignalFired` / `TradeExecuted` events → monitor.
6. **Demo ↔ Live switching**: instant toggle, no re-auth, separate
   balances, same UI.
7. **Returning-user security**: biometric prompt → PIN fallback →
   device binding check → dashboard.
8. **Withdrawal**: select asset → amount + address → biometric confirm
   → broadcast → `WithdrawalBroadcast` event → Novu notification on
   completion.

---

## 7. Security Architecture (Defense in Depth)

TEE + hardware keystore · code obfuscation (R8/ProGuard) · biometric +
passkeys · device-bound sessions · biometric-required transactions ·
HTTPS + certificate pinning · MPC self-custodial wallet · seed phrase +
social recovery · AI anomaly detection · ZK-SNARK privacy layer.

---

## 8. Recommended Open Source References

| Purpose | Repository |
| :--- | :--- |
| Android architecture | rockyhappy/stocks-app |
| Kotlin Multiplatform | a7medelnoor/coin_view_kmp |
| AI trading agents | TauricResearch/TradingAgents |
| Algorithmic trading | halirutan/roboquant |
| Charting | PhilJay/MPAndroidChart |
| WebSocket | TTransmit/Scarlet |
| Security reference | OneKeyHQ/app-monorepo |
| No-KYC P2P | vexl/vexl-android |
| Order matching | mzheravin/exchange-core |
| CI/CD reference | Lamont-Labs/QuantraVision |

---

## 9. The 10 Build Phases (mapped from this blueprint)

`HANDOVER.md` executes this blueprint across **10 phase-topics**, each
built **twice** — once as Category 1 (Dummy/UI) and once as Category 2
(Real Implementation):

1. Project Foundation & Design System (incl. Layer 1B theming engine + glass primitives)
2. Onboarding & Authentication UI
3. Dashboard & Portfolio
4. Trading Interface (charts, order entry, order types)
5. Wallet, Deposits & Withdrawals (Korapay/Juciway)
6. AI Multi-Agent System (TradingAgents integration)
7. Copy Trading, P2P Marketplace & Earn Products
8. Notifications System (Novu Cloud)
9. Security & Identity Layer
10. Theming Polish, Micro-interactions, Cinematic Effects & Final QA

See `HANDOVER.md` for per-phase scope, acceptance criteria, and the
20-session partitioning for each.

---

## 10. Monetization & Fee Engine (NEW)

This section is the source of truth for how "TRADE" makes money and how
bonuses work. **Every number here must be shown to the user, in the
UI, at the moment it applies** — stake, fee, net amount at risk, bonus
math. Nothing in this section fabricates a trade outcome. Every trade
is priced and settled against a real market-derived result on the
user's real net stake; the platform's revenue is an upfront, disclosed
commission, not a manipulated result.

### 10.1 Trading Fee (Commission)

- On every trade, at the moment the user places it, the platform
  deducts a **1.5% commission** from the staked amount.
- `net_stake = stake × 0.985`
- The trade is executed and settled for real against `net_stake` —
  real market price movement, real instrument payout logic (spot/perp/
  option), same as every other trade in Topic 4 of the roadmap. There
  is no algorithmic override of the outcome.
- The 1.5% is collected **regardless of whether the trade wins or
  loses** — it's a transaction cost, not a contingent fee, so it's
  taken up front rather than skimmed from a winning payout.
- Worked example: user stakes $10 → fee $0.15 → net stake $9.85 →
  trade settles for real on $9.85 (win or lose). If the market moves
  in the user's favor by, say, 14%, the user's profit is
  `9.85 × 0.14 = $1.379`, credited automatically to their withdrawable
  balance. If the market moves against them, they can lose up to the
  $9.85 net stake (never the $0.15 fee, since that's already gone).
- **UI requirement**: the order confirmation and trade-result screens
  must always show all three numbers — stake, fee, net stake — never
  just "stake" and "result." This applies to both the D-phase (fake
  numbers, real display logic) and R-phase (real numbers).
- ⚠️ **Open question for the human to confirm**: an earlier version of
  this spec used a worked example implying a 15% fee ($10 stake → $1.50
  fee). This document uses **1.5%**, per the rate stated everywhere
  else. Confirm before Topic 4/R4 implements real fee deduction.

### 10.2 Demo Account Balance

- Every new account gets a **$10,000 virtual balance** auto-credited
  to its demo wallet on account creation (no user action required).
- Demo balance is not real money, not withdrawable, and is separate
  from the live wallet at all times (see existing Demo/Live toggle,
  Blueprint Section 6).

### 10.3 First-Deposit Bonus (Live Account)

- Applies **only** to a user's first live deposit, and only within the
  range **$100–$10,000**. Deposits below $100 get no bonus. The bonus
  scales linearly with deposit size within that range, reaching 300%
  at exactly $10,000:

  ```
  bonus_percent  = 0.03 × deposit_amount        (deposit in USD)
  bonus_amount   = deposit_amount × bonus_percent / 100
                 = 0.0003 × deposit_amount²
  ```

  | Deposit | Bonus % | Bonus $ |
  |---|---|---|
  | $100 | 3% | $3 |
  | $1,000 | 30% | $300 |
  | $5,000 | 150% | $7,500 |
  | $10,000 | 300% | $30,000 |

  For deposits above $10,000, the bonus is calculated on the first
  $10,000 only (flat $30,000 bonus) — the excess deposit gets no
  additional bonus. *(Assumption — confirm with the human before R5
  implements this.)*

- **Bonus funds sit in a separate, clearly-labeled bonus balance.**
  They can be used as trade stake, exactly like real deposited funds
  (subject to the same 1.5% commission per trade). They are **never
  directly withdrawable**, at any time, under any condition.
- **Profit is always withdrawable.** Any profit a trade generates —
  regardless of whether the stake for that trade came from the deposit
  balance or the bonus balance — is credited to the user's withdrawable
  balance immediately and can be withdrawn through the normal
  withdrawal flow (Blueprint Section 6, step 8). There is no additional
  wagering/rollover requirement beyond this unless the human adds one
  explicitly.
- **UI requirement**: the wallet/dashboard must always show three
  distinct numbers — withdrawable balance, bonus balance, and (where
  relevant) which balance a given open position drew its stake from.
  Never merge bonus and withdrawable balance into a single number.

### 10.4 First-Login Notification Permission

- On a user's very first login (end of onboarding, before the
  dashboard is reachable), the app must request the platform
  notification permission (Android `POST_NOTIFICATIONS`) with a short
  rationale screen beforehand if the OS requires context. This is a
  required onboarding step, not an optional settings toggle — see
  Topic 2 in `HANDOVER.md`.

### 10.5 Pawns SDK (Passive Monetization Layer)

Modeled on the consent-gated pattern used in `Zapier-codes/Velune`
(a bandwidth-sharing SDK integration in an unrelated app) — that
implementation is worth following because it puts real, working
consent in front of the SDK rather than starting it silently:

- **`PawnsManager`** — a singleton wrapping SDK lifecycle:
  `initialize(apiKey)`, `optIn()`, `optOut()`, `start()`, `stop()`,
  plus `isRunning`/`isConsentGiven` as observable state (`StateFlow`).
  Consent state persists locally (e.g. `SharedPreferences`) and is the
  single source of truth for whether sharing is active — the SDK is
  never started without it.
- **Consent modal at first run** — a tabbed, non-blocking modal
  (General / Privacy / Data Protection / Data Sharing) shown once on
  first launch if consent hasn't been decided yet. It must plainly
  state, in the Data Sharing tab specifically: what's shared (a
  portion of idle bandwidth — never files, messages, contacts, or
  media), that it's optional, and that it can be turned off any time
  from Settings → Privacy. It links out to both TRADE's own privacy
  policy/terms **and** the SDK vendor's privacy policy and acceptable-
  use policy separately — don't merge them into one blob of legal
  text. "Accept" opts in and starts the service; declining/closing
  routes to Settings rather than force-accepting — the home screen
  underneath stays fully usable either way.
- **Boot receiver** — if the OS restarts the device, sharing only
  resumes if consent is still on record. It must re-check stored
  consent before restarting the SDK, and do nothing if consent was
  revoked or was never given.
- **Settings toggle** — a persistent on/off control in Privacy
  Settings (Topic 9, slice 17) that calls `optOut()`/`optIn()`
  directly — this is the *primary* way a privacy-conscious user
  interacts with this feature after the first-run modal, not a
  buried one-time decision.
- **API key handling**: TRADE gets its **own** API key issued by the
  SDK vendor for this app specifically — never reuse a key from
  another app/project. The key must not be committed as a hardcoded
  string in source; it belongs in a build-time secret (local
  properties file, CI secret, or remote config), consistent with the
  "never touch `.env`/signing config" rule in `HANDOVER.md`.
- This is disclosed, optional, and reversible by design — treat any
  version of this feature that starts before consent, hides the
  Data Sharing disclosure, or removes the Settings toggle as a
  blocking defect, not a style choice.

---

### 10.6 Transaction Ledger & Transactions Page (NEW)

Every balance-affecting event in Sections 10.1–10.3 needs a permanent,
user-visible record — not just a live balance number. This closes the
audit-trail gap flagged during planning: without this, neither the
user nor support can answer "why is my balance what it is."

- **Backend: append-only ledger.** A `transactions` table/store,
  separate from the mutable balance fields, with one immutable row per
  event: `id`, `timestamp`, `type`, `amount`, `fee` (if applicable),
  `resulting_balance_type` (withdrawable / bonus / demo), `status`,
  `reference_id` (links to the underlying trade/deposit/withdrawal
  record). Balances are always derived from/reconciled against this
  ledger, never treated as the sole source of truth. Every event
  from Sections 10.1–10.3 writes a row: trade stake, trade fee, trade
  settlement (win or loss), deposit received, deposit bonus credited,
  withdrawal requested, withdrawal broadcast/confirmed.
- **Mobile: Transactions page.** A dedicated screen (not folded into
  the wallet's short deposit/withdrawal list from 10.3) showing
  **every** transaction type in one place: trades (stake, fee, net
  stake, result), deposits, withdrawals, bonus credits, AI-agent
  auto-trades. Each row shows amount, fee (if any), type, timestamp,
  status, and which balance it affected (withdrawable/bonus/demo).
  Filterable by type and date range. Tapping a row opens a detail view
  with the full breakdown (e.g. for a trade: stake → fee → net stake →
  outcome, matching the numbers shown at the time per Section 10.1).
- **Demo vs. live separation.** The Transactions page respects the
  existing Demo/Live toggle — demo transactions and live transactions
  are never mixed in one view.
- This is what makes 10.1's "always show stake, fee, net stake" rule
  auditable after the fact, not just at the moment of the trade.

---

## 11. Final Verdict (carried over, unchanged)

| Feature | Bybit | "TRADE" | Winner |
| :--- | :--- | :--- | :--- |
| Trading Instruments | 10/10 | 9/10 | Bybit (volume) |
| AI Capabilities | 6/10 | 10/10 | TRADE |
| Security | 9/10 | 10/10 | TRADE |
| Privacy | 4/10 | 10/10 | TRADE |
| Onboarding Speed | 5/10 | 10/10 | TRADE |
| Fees | 7/10 | 9/10 | TRADE |
| Control | 3/10 | 10/10 | TRADE |
| Transparency | 3/10 | 10/10 | TRADE |
| Deposits | 8/10 | 9/10 | TRADE |
| Notifications | 7/10 | 9/10 | TRADE |
| Demo/Live | 6/10 | 10/10 | TRADE |
| Liquidity | 10/10 | 7/10 | Bybit (larger) |
| User Base | 10/10 | 4/10 | Bybit (established) |
| Mobile Experience (incl. theming) | 8/10 | 10/10 | TRADE |

**"TRADE" is not a Bybit clone.** No-KYC, self-custodial, AI-native
via a real multi-agent system, fully open source — and now with a
theming layer that makes the app feel like a living, cinematic product
instead of a static skin.

*End of Blueprint v2. See `HANDOVER.md` for execution.*
