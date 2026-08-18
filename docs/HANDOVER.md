# HANDOVER.md — "TRADE" Build (Dummy-First, Then Real)

**Read this file FIRST, in full, before touching any code.**
This project builds "TRADE" (see companion `TRADE_BLUEPRINT_v2.md` for
the full product spec) in **two categories**, each covering the same
**10 phase-topics**:

- **Category 1 — DUMMY** (phases D1–D10): every screen, component, and
  flow gets built with **static/mock data and fake logic only**. No
  real backend calls, no real AI microservice, no real payment
  gateway, no real crypto keys. The goal is a fully clickable,
  visually complete prototype — including the full dynamic theming
  engine (Layer 1B in the blueprint), since that's a UI concern, not a
  backend one.
- **Category 2 — REAL** (phases R1–R10, same 10 topics in the same
  order): replace every mock in Category 1 with the real
  implementation — real TradingAgents microservice, real Korapay/
  Juciway integration, real MPC wallet, real Novu notifications, etc.

**Category 2 does not start until every phase in Category 1 is ✅.**
Within a category, phases are done in order (D1→D10, then R1→R10).

Each phase-topic is further split into **20 sessions**. Each AI session
does **one slice of one phase** (see "Session partitioning" below),
unless the human explicitly asks for more. This means the full build is
planned as **20 phases × 20 sessions = up to 400 sessions**, though a
session may finish more than one slice if the slice is small — always
say so explicitly in your handoff and update the tables accordingly
rather than silently doing extra scope.

If you are an AI reading this: scroll to **"CURRENT STATUS"** to find
the exact phase and slice to do next. Do not skip ahead. Do not redo a
slice marked ✅. Do not silently expand scope into a later phase or
into Category 2 while Category 1 is incomplete.

---

## 0. REPO BASICS

- **Repo:** `https://github.com/Zapier-codes/Trade` — **live.** Created
  and initialized in Category 1 / Phase D1 / Slice 1 (see patch #0001).
  Every session after that clones this URL — do not re-create the
  repo.
- **Owner/dev (human):** works from a local dev machine, applies
  `git format-patch`-style `.patch` files via `git am <patch> && git
  push`, same workflow as this project's sibling projects. Confirm
  with the human if this differs.
- **Monorepo layout** (create in Phase D1):
  ```
  Trade/
    android/                # Kotlin + Jetpack Compose app
    services/
      trading-agents/       # Python microservice (TradingAgents)
      backend/              # Ktor/Spring Boot API gateway + services
    docs/
      TRADE_BLUEPRINT_v2.md
      HANDOVER.md
  ```
  If a later session finds strong reason to split into multiple repos
  (e.g. Android CI needs its own repo), that's a Phase D1/R1-scope
  decision only — flag it loudly and get human confirmation first; do
  not restructure repo layout mid-build.
- **Stack:** Kotlin, Jetpack Compose, Clean Architecture + MVVM,
  StateFlow, Compose Navigation. Backend: Ktor or Spring Boot,
  PostgreSQL, Redis, RabbitMQ/Kafka. AI: Python microservice
  (TradingAgents framework) served via REST + WebSocket. See
  `TRADE_BLUEPRINT_v2.md` Section 3 for full architecture.
- **App identity:** Privacy-first, no-KYC, self-custodial, AI-native
  trading app. Full feature/positioning spec in
  `TRADE_BLUEPRINT_v2.md`.

### Clone instructions (for every session after Phase D1/Slice 1)

```bash
git clone https://github.com/Zapier-codes/Trade.git
cd Trade
git log --oneline -5        # sanity check you're on latest
cat docs/HANDOVER.md        # this file — always re-read fresh, don't trust memory
```

Do **not** attempt to run Gradle builds, boot an emulator, or run the
Python service live in a sandbox — there is no working device/emulator
in these AI sessions. Work is source-only: read, edit, patch. The
human builds and runs on their own machine/device.

---

## 1. THE WORKFLOW (every session follows this exactly)

1. **Clone** the repo fresh (instructions above). If the repo doesn't
   exist yet, you are Phase D1/Slice 1 — create it per Section 0.
2. **Read `docs/HANDOVER.md`** in the cloned repo (not from memory),
   find the current phase + slice in "CURRENT STATUS."
3. **Read that phase's Goal/Scope/Acceptance in Section 4**, and find
   your specific slice in that phase's 20-slice table. Stay inside
   that slice's scope. If you discover something broken or
   out-of-scope, **note it in "Known Issues / Notes for Next Session"
   (Section 6)** — don't fix it yourself unless it blocks your slice.
4. **Do the work**: edit files directly in the cloned repo.
5. **Commit** your work:
   ```bash
   git add -A
   git commit -m "D1.S03: <short description>"   # PhaseID.Slice#: desc
   ```
6. **Generate a patch** for the human:
   ```bash
   git format-patch -1 HEAD --start-number <NNNN> -o /mnt/user-data/outputs/
   ```
   `<NNNN>` is the next number in the patch sequence — check the
   status table for the last patch number used, increment by one.
7. **Present the patch file** via `present_files`. Tell the human
   plainly: which phase/slice this was, what changed (short bullets),
   and the exact apply command:
   ```
   git am ~/storage/downloads/0007-D1.S03-glass-token-primitives.patch && git push
   ```
   (Path is `~/storage/downloads/` for the Termux workflow this project
   uses — not `~/Downloads/` — confirmed 2026-08-18. Requires Termux's
   `termux-setup-storage` to have been run once so `~/storage/downloads`
   is linked to the device's real Downloads folder.)
8. **Update `docs/HANDOVER.md` itself**, in the same commit:
   - Mark your slice ✅ in that phase's slice table (date + patch #).
   - If that was the 20th slice of the phase, mark the phase itself
     ✅ in the CURRENT STATUS table and move the "NEXT SESSION" pointer
     to the next phase.
   - Fill in Section 6 if relevant.

### Session partitioning — how a "slice" works

Each phase-topic has a **20-slice table** in Section 4 (one shared
table per topic, used by both its D-phase and its R-phase — see
below). A slice is a named, roughly-equal-effort sub-scope, not a
strict line-by-line spec — use judgment on exact files/components
within the slice's stated area, same as any other phase in this kind
of document. Take the next unclaimed (🔲) slice in order unless the
human directs otherwise. If a slice turns out too big for one session,
split it (e.g. `D4.S07a`, `D4.S07b`) and say so explicitly in your
handoff, updating the table to show both.

**Category/phase slices share the same 20-slice breakdown by name**
(e.g. "S07 — Order entry panel" exists for both D4 and R4), but the
*acceptance bar* differs:
- **D-phase (Dummy) slices**: build the real UI/UX for that slice with
  **mock/static data** — hardcoded sample values, in-memory fake
  "repositories" that return canned responses (optionally with a fake
  `delay()` to simulate latency), no network calls, no real crypto/AI/
  payment logic. Interactions should look and feel completely real —
  animations, theming events (Section 3B of the blueprint), state
  transitions — just backed by fake data.
- **R-phase (Real) slices**: same named area, now wired to the real
  thing per `TRADE_BLUEPRINT_v2.md` — real API/service calls, real
  TradingAgents responses, real payment gateway, real wallet/keys,
  real Novu triggers. The UI built in the D-phase slice should mostly
  just need its data layer swapped — if it needs a significant UI
  rewrite to accommodate the real data shape, that's fine, but note
  why in your handoff (it may mean the D-phase slice under-specified
  the real API contract for a future project's lessons-learned).

### Patch numbering

Check the highest patch number recorded in the status tables below.
Increment by one. If unsure, ask the human "what was the last patch
number you applied?" before generating yours.

### Non-negotiable rules for every session

- **One slice per session**, unless the human explicitly asks for more
  in the same turn.
- **Category 2 does not start until Category 1's status table shows
  all 10 phases ✅.** If you are asked to jump ahead, push back and
  point at this rule; only proceed if the human explicitly overrides
  it in writing in this file's Known Issues section.
- **Never re-architect a previous slice/phase's work** without saying
  so loudly and getting explicit confirmation — later slices build on
  earlier ones, especially Layer 1B (theming engine) which nearly
  everything depends on after Phase D1.
- **Every D-phase slice that shows the result of a user action must
  emit the matching `ThemeEvent`** (see Blueprint Section 3B.3) even
  though the data behind it is fake — the theming reaction is a UI
  behavior, not a backend one, and skipping it means Phase D10 (polish)
  has to redo earlier phases' work.
- **Never touch `.env`, secrets, keystore files, signing config, or CI/
  release files** unless a slice explicitly says to (this matters most
  from Phase R5/R9 onward).
- **Don't delete existing working functionality** to "clean up" unless
  the phase says to. Prefer additive, reversible changes.
- **Leave the repo in a state that would plausibly compile** — read
  your own diffs, check imports, check prop/parameter names match
  between composables/viewmodels.
- Treat this `HANDOVER.md` (in the repo, freshly cloned) and the repo's
  own commit history as **the only source of truth** for what's
  already done. Do not re-derive project status by reading the whole
  codebase from scratch — that's what this file and Section 6 are for.

---

## 2. CURRENT STATUS

### Category 1 — DUMMY (must complete fully before Category 2 starts)

| Phase | Title | Status | Slices Done | Last Patch # | Last Session Date |
|---|---|---|---|---|---|
| D1 | Project Foundation & Design System | 🟡 In progress | 5/20 (1a+1b+2+3+4+5 done) | 0009 | 2026-08-18 |
| D2 | Onboarding & Authentication UI | 🔲 Not started | 0/20 | — | — |
| D3 | Dashboard & Portfolio | 🔲 Not started | 0/20 | — | — |
| D4 | Trading Interface | 🔲 Not started | 0/20 | — | — |
| D5 | Wallet, Deposits & Withdrawals UI | 🔲 Not started | 0/20 | — | — |
| D6 | AI Multi-Agent System UI | 🔲 Not started | 0/20 | — | — |
| D7 | Copy Trading, P2P & Earn UI | 🔲 Not started | 0/20 | — | — |
| D8 | Notifications System UI | 🔲 Not started | 0/20 | — | — |
| D9 | Security & Identity UI | 🔲 Not started | 0/20 | — | — |
| D10 | Theming Polish, Micro-interactions & QA (Dummy) | 🔲 Not started | 0/20 | — | — |

### Category 2 — REAL (locked until Category 1 is 100% ✅)

| Phase | Title | Status | Slices Done | Last Patch # | Last Session Date |
|---|---|---|---|---|---|
| R1 | Project Foundation & Design System — Real | 🔒 Locked | 0/20 | — | — |
| R2 | Onboarding & Authentication — Real | 🔒 Locked | 0/20 | — | — |
| R3 | Dashboard & Portfolio — Real | 🔒 Locked | 0/20 | — | — |
| R4 | Trading Interface — Real | 🔒 Locked | 0/20 | — | — |
| R5 | Wallet, Deposits & Withdrawals — Real | 🔒 Locked | 0/20 | — | — |
| R6 | AI Multi-Agent System — Real | 🔒 Locked | 0/20 | — | — |
| R7 | Copy Trading, P2P & Earn — Real | 🔒 Locked | 0/20 | — | — |
| R8 | Notifications System — Real | 🔒 Locked | 0/20 | — | — |
| R9 | Security & Identity — Real | 🔒 Locked | 0/20 | — | — |
| R10 | Final Integration, Performance & QA (Real) | 🔒 Locked | 0/20 | — | — |

Legend: 🔲 not started · 🟡 in progress · ✅ complete · 🔒 locked (Category 2, waiting on Category 1)

**➡️ NEXT SESSION STARTS AT: Phase D1, Slice 6 — Typography + spacing
tokens** (type scale, spacing scale, in `core-theme` — rarely changes
once set, per the phase table).

---

## 3. WHY THIS SHAPE (dummy-first, 10×2, 20 slices)

- **Dummy-first** exists so the entire product surface — every screen,
  every flow, the full cinematic theming system — gets designed and
  reviewed by the human on real device hardware before any real
  money-moving or key-handling code exists. It's much cheaper to
  redesign a mock screen than a wired one.
- **10 phase-topics** map 1:1 to blueprint sections (see Blueprint
  Section 9) so Category 2 sessions can go straight to the matching
  blueprint section instead of re-deriving scope.
- **20 slices per phase** keeps each session's diff reviewable. A
  session that tries to build, say, all of Trading Interface in one
  pass produces a patch too large for the human to review carefully on
  a phone/Termux workflow — 20 slices keeps each patch focused.

---

## 3C. ADDENDUM — Monetization & Fee Engine (read before Topics 2/3/4/5/9)

This addendum was added after Topics 1–10 below were first written.
Full math and rationale live in `docs/TRADE_BLUEPRINT_v2.md` Section
10 — **read that section before touching any slice referenced here.**
This section only maps that spec onto specific slices so no session
misses it.

**Non-negotiable for every session touching these slices, D or R:**
trade outcomes are always real/market-derived on the user's net stake.
The platform's revenue is a disclosed, upfront 1.5% commission — never
a manipulated result. If you find yourself about to make a trade
outcome depend on anything other than real (or, in D-phase, realistic
simulated) market data plus the fee math below, stop and flag it in
Section 6 instead of building it.

- **Topic 2 (Onboarding) — Slice 7** (`Onboarding → Dashboard
  handoff`): must include the mandatory notification-permission
  request (Blueprint 10.4) as part of this step, before the dashboard
  is reachable. D-phase: fake permission prompt UI. R-phase: real
  Android `POST_NOTIFICATIONS` request + rationale screen.
- **Topic 3 (Dashboard) — Slice 1** (`Dashboard scaffold + demo/live
  header`): demo balance shown must be exactly **$10,000** (Blueprint
  10.2), auto-credited, not user-configurable.
- **Topic 4 (Trading) — Slices 7–13** (all order-entry + confirmation
  slices): every order confirmation and trade-result view must display
  stake, 1.5% fee, and net stake as three distinct numbers (Blueprint
  10.1) — not just stake and result. D-phase: fake numbers following
  the real formula (`net_stake = stake × 0.985`) so the UI is correct
  even though the market data is fake. R-phase: real fee deduction
  wired into real order submission, real settlement on `net_stake`.
- **Topic 5 (Wallet/Deposits) — Slice 10** (`Deposit success/fail
  states`): first-live-deposit bonus calculation (Blueprint 10.3) is
  computed and shown here on deposit success, for deposits $100–
  $10,000. D-phase: fake deposit amount → real bonus formula → fake
  credited-bonus animation. R-phase: real deposit amount from
  Korapay/Juciway webhook → real bonus formula → real bonus-balance
  credit.
- **Topic 5 — Slice 1** (`Wallet home / balance list`) and **Topic 3 —
  Slice 2** (`Portfolio value + 24h change card`): must show
  **withdrawable balance** and **bonus balance** as two separate
  numbers, never merged (Blueprint 10.3). This is a new sub-requirement
  on top of those slices' original scope — do it in the same slice,
  don't create a new one.
- **Topic 9 (Security & Identity) — Slice 17** (`Privacy settings
  screen`): must include a clear, plain-language disclosure that the
  Pawns SDK shares device bandwidth, plus an opt-in/opt-out toggle
  wired to `PawnsManager` (Blueprint 10.5). Treat missing disclosure
  or a missing toggle as blocking this slice's ✅, not a follow-up
  item.
- **New: Topic 1 — additional slice concern for D1/R1** (`Repo +
  Gradle scaffold` / DI wiring slices): scaffold `PawnsManager`,
  the tabbed `ConsentModal` (General/Privacy/Data Protection/Data
  Sharing tabs per Blueprint 10.5), and a boot receiver that only
  resumes sharing if consent is on record. D-phase: build the modal
  and manager with the SDK calls stubbed out (no real `.aar`/API key
  yet) so the consent UX can be reviewed before any real bandwidth
  sharing exists. R-phase: wire the real SDK, TRADE's own API key
  (from build-time secrets, never hardcoded — see Blueprint 10.5),
  and confirm the boot receiver actually checks consent before
  restarting.
- **New backend concern for R1/R4/R5**: a `FeeEngine` service
  (computes `net_stake`, applies bonus formula, tracks
  withdrawable-vs-bonus balance split) needs a real home in
  `services/backend`. R1 should scaffold its interface; R4/R5 wire
  trade settlement and deposit bonus crediting into it respectively.
  Flag in Section 6 if this warrants being pulled out as its own
  slice rather than folded into existing ones.

---

## 3D. ADDENDUM — Tooltip System & Dynamic Sound Engine (added post-D1.S04)

Full spec for both lives in `docs/TRADE_BLUEPRINT_v2.md` — the new
`GlassTooltip` entry in Section 3B.2, and the new **Layer 1C: Dynamic
Notification & Action Sound Engine** section (3B.4a in reading order,
between Layer 1B and Layer 2) — **read both before touching either
slice below.** This section only maps that spec onto specific slices,
same pattern as Section 3C.

**Non-negotiable for every session touching these slices, D or R:**
`SoundReactor` reuses Layer 1B's existing `ThemeEventBus` — do not
create a second event bus. Every event that already emits a
`ThemeEvent` must have a matching `SoundTokens` entry added in the
same patch, the same rule Section 3B.3 sets for the visual side. Sound
must never override device silent/DND mode.

- **Topic 1 (Foundation) — Slice 9** (`Glass primitives — Button/Dialog`):
  scope now also includes `GlassTooltip` (info/help overlay), built on
  Compose Material3's existing `TooltipBox`/`PlainTooltip`/`RichTooltip`
  — **no new Gradle dependency needed**, `material3` is already a
  dependency of every module that needs it. Reskin onto glass tokens
  from Slices 4-6 the same way the rest of Slice 9's primitives do.
- **Topic 1 (Foundation) — Slices 10-11** (`ThemeEventBus core` /
  `ThemeReactor — ambient glow`): scope now also includes scaffolding
  `SoundTokens` (Layer 1C.1) and `SoundReactor` (Layer 1C.2) alongside
  `ThemeEventBus`/`ThemeReactor` — they're built together because
  `SoundReactor` subscribes to the same bus `ThemeReactor` does. D-phase:
  `SoundReactor` wired and subscribed, but its actual playback can use
  short placeholder tones or system default notification sounds rather
  than final bundled assets (final asset selection is real creative work,
  flag as open in Section 6 if reached before R-phase). Playback uses
  native `android.media.SoundPool` + `AudioAttributes` — **no new Gradle
  dependency needed** for this either; don't reach for a third-party
  audio library for short UI cues.
- **Topic 8 (Notifications) — Slice 3** (`Notification settings screen`):
  add the in-app "sound on/off" toggle here (Layer 1C.2) alongside the
  per-category toggles already in scope. This is a new sub-requirement
  on top of that slice's original scope, same pattern 3C used — do it in
  the same slice, don't create a new one.
- **Topic 8 (Notifications) — Slices 4-8** (push notification triggers):
  when wiring each `NotificationChannel`'s sound (D-phase: fake local
  notification demo; R-phase: real FCM trigger), use the matching
  `SoundTokens` character from Layer 1C.1 so the in-app and
  out-of-app sound for the same event type feel consistent (Layer
  1C.3) — implemented separately from `SoundReactor`, per event.

---

## 4. PHASE DETAILS


Each phase-topic below has: **Goal**, **Scope**, **Phase acceptance
criteria** (all 20 slices done), and the **20-slice table** (shared
name/order between its D- and R- phase; see Section 1 for how the bar
differs).

### Topic 1 — Project Foundation & Design System (D1 / R1)

**Goal:** Stand up the monorepo, Clean Architecture skeleton, Compose
Navigation graph, and — critically — the full Layer 1B theming engine
(tokens, glass primitives, ThemeEventBus/ThemeReactor) so every later
phase has something to build on.

**Scope:** `android/app`, `android/core-theme`, `android/core-ui`
(glass primitives), `android/core-navigation`.

**Phase acceptance (D1):** app shell compiles conceptually, navigates
between empty placeholder screens for every planned route, dark/light
mode toggle works, `ThemeEventBus` exists and at least one placeholder
screen demonstrably reacts to a test event with both a dark- and
light-mode expression.

**Phase acceptance (R1):** any real infra decisions the dummy skipped
(DI framework choice — Hilt/Koin —, module boundaries, build variants
for demo/live) are finalized and real; no mock `ThemeEventBus` — same
one from D1, unchanged unless a genuine bug is found.

| # | Slice | D-phase focus | R-phase focus |
|---|---|---|---|
| 1a | Repo creation (Section 0) | Create repo, push initial `docs/HANDOVER.md` + `docs/TRADE_BLUEPRINT_v2.md` — **done, patch #0001** | — (one-time) |
| 1b | Gradle scaffold + module structure + Pawns/Consent scaffold | Module skeleton (`app`, `core-theme`, `core-ui`, `core-navigation`), empty app that plausibly compiles, `PawnsManager`/`ConsentModal`/boot-receiver stubs per Section 3C addendum (SDK calls stubbed, no real `.aar`/API key) | Verify/finalize build config, CI hooks; wire real Pawns SDK + API key + boot-receiver consent check |
| 2 | Clean Architecture skeleton | `domain`/`data`/`presentation` folder contracts, placeholder use-cases | Real DI wiring (Hilt/Koin) across layers |
| 3 ✅ | Navigation graph | All planned routes as empty Composables — **done, patch #0005** | Deep-link handling, real auth-gated routes |
| 4 ✅ | Color tokens (dark) | `TradeThemeDark` full token set — **done, patch #0006** | Contrast/accessibility audit against real content |
| 5 | Color tokens (light) ✅ (0009) | `TradeThemeLight` full token set | Contrast/accessibility audit against real content |
| 6 | Typography + spacing tokens | Type scale, spacing scale | — (rarely changes; verify only) |
| 7 | Glass primitives — Surface/Card | `GlassSurface`, `GlassCard` | Perf pass (blur cost on real devices) |
| 8 | Glass primitives — AppBar/Sheet | `GlassAppBar`, `GlassBottomSheet` | Perf pass |
| 9 | Glass primitives — Button/Dialog (+ Tooltip, see 3D) | `GlassButton`, `GlassDialog`, `GlassTooltip` | Perf pass |
| 10 | ThemeEventBus core (+ SoundTokens/SoundReactor scaffold, see 3D) | `SharedFlow<ThemeEvent>`, event sealed class, `SoundTokens`, `SoundReactor` subscribed | Confirm real screens emit correctly end-to-end; real bundled sound assets |
| 11 | ThemeReactor — ambient glow | Animate `ambientGlow` color per event | Tune against real event frequency |
| 12 | ThemeReactor — light source motion | Animate `lightSource.x/y` | Tune |
| 13 | ThemeReactor — contextual caption | `ContextualCaption` slot + fade timing | Real copywriting pass per event |
| 14 | Dark/light parity pass | Verify every event has both expressions (3B.4) | Verify no regressions |
| 15 | Demo/Live toggle shell | Toggle UI + fake balance switch, no persistence | Real persistence, real balance source |
| 16 | App icon / splash placeholder | Placeholder branding assets | Real branding assets |
| 17 | Bottom nav / tab bar shell | Glass tab bar, placeholder icons | Real icon set, real active-route logic |
| 18 | Error/empty state system | Reusable `EmptyState`/`ErrorState` components | Wire to real error taxonomy |
| 19 | Loading/skeleton system | Reusable skeleton components | Wire to real loading states |
| 20 | Phase QA + docs | Screenshot every screen both modes, update this file | Full regression pass, update this file |

---

### Topic 2 — Onboarding & Authentication UI (D2 / R2)

**Goal:** Zero-click onboarding flow and biometric/passkey auth UI.

**Scope:** `android/feature-onboarding`, `android/feature-auth`.

**Phase acceptance (D2):** full onboarding swipe flow + ToS screen +
fake biometric prompt UI + fake device-binding indicator, all
navigable, all themed per Layer 1B.

**Phase acceptance (R2):** real Android Credential Manager + Passkeys,
real Keystore/TEE device binding, real biometric fallback to PIN.

| # | Slice | D-phase focus | R-phase focus |
|---|---|---|---|
| 1 | Welcome screen | Branded full-screen welcome | Finalize copy/assets |
| 2 | Tutorial slide 1 (AI trading) | Interactive Compose tutorial | — |
| 3 | Tutorial slide 2 (deposits) | Interactive Compose tutorial | — |
| 4 | Tutorial slide 3 (charts) | Interactive Compose tutorial | — |
| 5 | Tutorial nav/pager polish | Swipe physics, indicators | Perf pass |
| 6 | Minimal ToS screen | ToS accept UI | Real legal copy plumbing |
| 7 | Onboarding → Dashboard handoff | Fake "account created" transition | Real account/device identity creation |
| 8 | Biometric prompt UI | Fake face/fingerprint prompt | Real Credential Manager integration |
| 9 | PIN fallback UI | Fake PIN entry + validation | Real PIN storage (Keystore-backed) |
| 10 | Device binding indicator | Fake "verifying device" animation | Real Keystore/TEE fingerprinting |
| 11 | Passkey setup UI | Fake passkey creation flow | Real Passkeys API |
| 12 | Returning-user auth flow | Biometric→PIN fallback→dashboard, fake | Real flow, real session tokens |
| 13 | Seed phrase display UI | Fake seed phrase screen + warnings | Real MPC-backed seed generation (coordinate w/ R9) |
| 14 | Social recovery setup UI | Fake contact-picker flow | Real recovery mechanism (coordinate w/ R9) |
| 15 | Session/device management screen | List of fake "trusted devices" | Real device session list |
| 16 | Logout / device unbind flow | Fake confirmation + animation | Real revoke logic |
| 17 | Error states (auth failures) | Fake failure UI (wrong PIN, biometric fail) | Real error taxonomy |
| 18 | Theming events for auth | Success/failure ThemeEvents | Verify real triggers |
| 19 | Accessibility pass | Screen reader labels, text scaling | Verify with real content |
| 20 | Phase QA + docs | Full flow walkthrough, screenshots, update file | Full regression, update file |

---

### Topic 3 — Dashboard & Portfolio (D3 / R3)

**Goal:** The widget-based real-time dashboard — the app's home base.

**Scope:** `android/feature-dashboard`.

**Phase acceptance (D3):** full dashboard layout with fake portfolio
value, fake 24h change, fake allocation chart, fake AI agent status
panel, fake live market feed ticker, fake AI summary, fake
notifications badge — all themed, all reacting to test events.

**Phase acceptance (R3):** every widget backed by real data sources
(real portfolio balances, real price feed, real agent status stream).

| # | Slice | D-phase focus | R-phase focus |
|---|---|---|---|
| 1 | Dashboard scaffold + demo/live header | Layout shell, fake balances (USD/EUR) | Real multi-currency balance source |
| 2 | Portfolio value + 24h change card | Fake numbers, fake up/down coloring | Real portfolio calc |
| 3 | Quick actions row | Deposit/Withdraw/Trade/AI Agent buttons | Wire real navigation targets |
| 4 | Portfolio allocation chart | Fake pie/bar chart (BTC/ETH/USDT/AI %) | Real allocation data |
| 5 | AI agent status panel shell | "4 agents running" fake panel | Real agent status stream (coordinate w/ D6/R6) |
| 6 | AI agent breathing glow/shadow | Layer 1B `AgentStatusChange` reaction | Tune against real event cadence |
| 7 | Today's P&L + ROI card | Fake numbers | Real P&L calc |
| 8 | AI signal callout | Fake "BUY BTC @ $67,450 (87%)" card | Real signal feed (coordinate w/ D6/R6) |
| 9 | Live market feed ticker (shell) | Fake scrolling ticker, static prices | Real price feed WebSocket |
| 10 | Live market feed (per-asset detail tap) | Fake mini detail sheet | Real per-asset data |
| 11 | AI market summary card | Fake generated-sounding paragraph | Real AI-generated summary |
| 12 | Notification bell + badge count | Fake count, fake dropdown preview | Wire to real Novu feed (coordinate w/ D8/R8) |
| 13 | Pull-to-refresh | Fake refresh animation/delay | Real refetch logic |
| 14 | Empty/loading states | Skeletons for every widget | Wire to real loading states |
| 15 | Portfolio history mini-chart | Fake sparkline | Real historical data |
| 16 | Currency switcher | Fake USD/EUR/etc. switch | Real localization (IPGeolocation.io) |
| 17 | Dashboard-wide theming events | Wire `TradeExecuted`/`DepositConfirmed` reactions on dashboard | Verify real triggers |
| 18 | Widget reordering / customization (if in scope) | Fake drag-to-reorder | Real persisted layout prefs |
| 19 | Accessibility pass | Labels, scaling, contrast | Verify with real content |
| 20 | Phase QA + docs | Screenshots both modes, update file | Full regression, update file |

---

### Topic 4 — Trading Interface (D4 / R4)

**Goal:** Charting, order entry, order types, AI overlays.

**Scope:** `android/feature-trading`.

**Phase acceptance (D4):** full trading screen — pair selector, chart
(MPAndroidChart/TradingView WebView shell), AI overlay toggle, order
entry panel with all order types, demo/live mode, fake order
confirmation — fully clickable with fake fills.

**Phase acceptance (R4):** real price data, real order routing/
matching, real AI overlays from TradingAgents.

| # | Slice | D-phase focus | R-phase focus |
|---|---|---|---|
| 1 | Pair selector | Fake pair list, search/filter | Real instrument list |
| 2 | Chart shell (candlestick) | MPAndroidChart w/ fake OHLC data | Real price history feed |
| 3 | Chart shell (TradingView WebView) | Static/fake TradingView embed | Real TradingView data feed |
| 4 | Chart timeframe switcher | Fake timeframe buttons | Real timeframe data fetch |
| 5 | AI overlay toggle + rendering | Fake overlay markers on chart | Real AI analysis overlay (coordinate w/ D6/R6) |
| 6 | Order book widget | Fake bid/ask ladder | Real order book stream |
| 7 | Order entry — Market | Fake market order form | Real market order submission |
| 8 | Order entry — Limit | Fake limit order form | Real limit order submission |
| 9 | Order entry — Stop-Loss/TP | Fake SL/TP form | Real SL/TP submission |
| 10 | Order entry — Trailing/OCO | Fake trailing/OCO form | Real trailing/OCO submission |
| 11 | Smart Order (AI-optimized) entry | Fake "AI-assisted price" suggestion | Real AI-optimized pricing (coordinate w/ D6/R6) |
| 12 | Leverage selector (perp/futures) | Fake leverage slider w/ safety-limit UI | Real leverage + margin calc |
| 13 | Order confirmation sheet | Fake confirm showing stake/1.5% fee/net stake (see Section 3C) → fake fill animation on net stake | Real submission on net stake → real fill/ack, real fee deducted |
| 14 | Open orders list | Fake list, fake cancel action | Real order status stream |
| 15 | Trade history list | Fake past trades | Real trade history |
| 16 | Position management panel | Fake open positions, fake close button | Real position data + real close |
| 17 | Demo/live mode wiring for trading | Fake balance draws from correct wallet | Real balance draws |
| 18 | Trade-result theming events | `TradeExecuted` reaction on fill | Verify real trigger timing |
| 19 | Options/RWA/structured product shell | Fake product list + fake entry form | Real product integration (Premia/Derive) |
| 20 | Phase QA + docs | Full trade flow walkthrough, update file | Full regression, update file |

---

### Topic 5 — Wallet, Deposits & Withdrawals (D5 / R5)

**Goal:** Multi-chain wallet UI, Korapay + Juciway deposit flows,
withdrawal flow.

**Scope:** `android/feature-wallet`.

**Phase acceptance (D5):** full deposit method picker → amount entry →
fake payment screen → fake success/fail states; full withdrawal flow
with fake broadcast; wallet balance list — all fake data, fully themed.

**Phase acceptance (R5):** real Korapay session creation + CustomTabs/
WebView flow, real Juciway/Hyperswitch Payment Sheet, real withdrawal
broadcast, real multi-chain wallet balances (self-custodial, MPC keys
per Topic 9).

| # | Slice | D-phase focus | R-phase focus |
|---|---|---|---|
| 1 | Wallet home / balance list | Fake multi-chain balance list | Real balance aggregation |
| 2 | Deposit entry point + method picker | Fake method list (card/bank/mobile/crypto/Interac) | Wire real method availability by region |
| 3 | Deposit amount entry + currency conversion | Fake IP-based conversion display | Real IPGeolocation.io conversion |
| 4 | Korapay flow — card | Fake CustomTabs/WebView mock screen | Real Korapay session + webview |
| 5 | Korapay flow — bank transfer | Fake bank transfer instructions screen | Real Korapay bank flow |
| 6 | Korapay flow — mobile money | Fake mobile money screen | Real Korapay mobile money flow |
| 7 | Juciway flow — Hyperswitch sheet shell | Fake payment sheet UI | Real Hyperswitch SDK integration |
| 8 | Juciway flow — crypto deposit | Fake crypto deposit address screen | Real crypto deposit address generation |
| 9 | Juciway flow — Interac e-Transfer | Fake Interac screen | Real Interac flow |
| 10 | Deposit success/fail states | Fake success screen + `DepositConfirmed` event + first-deposit bonus calc/display (see Section 3C, Blueprint 10.3) | Real webhook-driven success/fail + real bonus credit to separate bonus balance |
| 11 | Withdrawal — asset/amount/address entry | Fake form + fake address validation | Real address validation |
| 12 | Withdrawal — biometric confirm step | Fake biometric gate (reuse Topic 2 shell) | Real biometric gate |
| 13 | Withdrawal — broadcast + status | Fake "broadcasting" animation → fake tx hash | Real broadcast + real tx tracking |
| 14 | Withdrawal theming event | `WithdrawalBroadcast` reaction | Verify real trigger |
| 15 | Transaction history (deposits+withdrawals) | Fake combined history list | Real history from backend |
| 16 | Fee display / estimator | Fake fee breakdown | Real fee calc (network + platform) |
| 17 | Multi-chain network selector | Fake chain picker | Real chain support matrix |
| 18 | Wallet security prompts (backup reminder etc.) | Fake reminder banners | Real backup-state-aware prompts |
| 19 | Error states (failed payment, insufficient funds) | Fake error UI + `ErrorOccurred` event | Real error taxonomy |
| 20 | Phase QA + docs | Full deposit/withdraw walkthrough, update file | Full regression, update file |

---

### Topic 6 — AI Multi-Agent System (D6 / R6)

**Goal:** UI/integration surface for the TradingAgents multi-agent
system (Market Analyst, Sentiment, Trader, Portfolio Manager, Risk
Manager).

**Scope:** `android/feature-ai-agents`, `services/trading-agents`
(Python microservice — scaffolded in D6, made real in R6).

**Phase acceptance (D6):** full AI Agent activation flow (strategy
picker, risk tolerance, activate), agent status dashboard, fake signal
feed, fake backtest results screen — all fake data; Python service
directory scaffolded with clear interface stubs (not implemented) so
R6 has a defined contract to fill in.

**Phase acceptance (R6):** Python microservice actually implements the
5 agents, real REST/WebSocket wired end-to-end, real LLM provider
calls, real backtesting engine.

| # | Slice | D-phase focus | R-phase focus |
|---|---|---|---|
| 1 | AI Agent entry point + strategy picker | Fake strategy list (Scalp/Momentum/Grid/Arbitrage) | Real strategy definitions |
| 2 | Risk tolerance selector | Fake Low/Medium/High UI | Wire to real Risk Manager agent params |
| 3 | Activation flow + confirmation | Fake "agents activating" animation | Real activation call |
| 4 | Agent status dashboard — Market Analyst card | Fake status/output | Real agent output |
| 5 | Agent status dashboard — Sentiment card | Fake status/output | Real agent output |
| 6 | Agent status dashboard — Trader card | Fake status/output | Real agent output |
| 7 | Agent status dashboard — Portfolio Manager card | Fake status/output | Real agent output |
| 8 | Agent status dashboard — Risk Manager card | Fake status/output | Real agent output |
| 9 | Live signal feed UI | Fake scrolling signal list | Real WebSocket signal stream |
| 10 | Signal detail sheet (confidence, reasoning) | Fake reasoning text | Real LLM-generated reasoning |
| 11 | Backtesting screen — config | Fake strategy/date-range picker | Real backtest request |
| 12 | Backtesting screen — results | Fake equity curve + stats | Real historical simulation results |
| 13 | Python service scaffold — API contracts | Define REST/WS interface stubs, no logic | — |
| 14 | Python service — Market Analyst agent | — | Real TA indicator implementation |
| 15 | Python service — Sentiment agent | — | Real NLP/social scraping implementation |
| 16 | Python service — Trader agent | — | Real RL signal generation |
| 17 | Python service — Portfolio Manager agent | — | Real MPT/genetic allocation |
| 18 | Python service — Risk Manager agent | — | Real VaR/Monte Carlo |
| 19 | AISignalFired theming event wiring | Fake-triggered event for UI demo | Verify real trigger timing/frequency |
| 20 | Phase QA + docs | Full AI flow walkthrough, update file | Full regression incl. Android↔Python integration test, update file |

---

### Topic 7 — Copy Trading, P2P & Earn Products (D7 / R7)

**Goal:** Copy trading (AI + social), zero-fee P2P marketplace, earn
products (savings/staking/AI yield farming).

**Scope:** `android/feature-copy-trading`, `android/feature-p2p`,
`android/feature-earn`.

**Phase acceptance (D7):** all three sub-features fully navigable with
fake data (fake trader leaderboard, fake P2P listings/escrow flow,
fake earn product list + fake staking flow).

**Phase acceptance (R7):** real smart-contract-based copy trading,
real P2P escrow engine, real Aave/Compound-backed earn products.

| # | Slice | D-phase focus | R-phase focus |
|---|---|---|---|
| 1 | Copy trading — leaderboard | Fake trader leaderboard | Real trader performance data |
| 2 | Copy trading — trader profile | Fake profile + stats | Real on-chain performance verification |
| 3 | Copy trading — follow/allocate flow | Fake follow + fake allocation slider | Real smart-contract follow tx |
| 4 | Copy trading — active copies dashboard | Fake P&L per copied trader | Real P&L tracking |
| 5 | Social copy trading feed | Fake social feed of trade calls | Real feed source |
| 6 | P2P — listing browse | Fake buy/sell listings | Real listing data |
| 7 | P2P — create listing | Fake create-listing form | Real listing creation |
| 8 | P2P — escrow flow (buyer side) | Fake escrow steps | Real escrow smart contract |
| 9 | P2P — escrow flow (seller side) | Fake escrow steps | Real escrow smart contract |
| 10 | P2P — dispute/chat UI | Fake chat thread | Real messaging + dispute flow |
| 11 | Earn — product list (savings) | Fake APY list | Real Aave/Compound rates |
| 12 | Earn — product list (staking) | Fake staking options | Real staking integration |
| 13 | Earn — AI-optimized yield farming | Fake "AI-optimized" badge + fake allocation | Real AI-driven yield allocation (coordinate w/ D6/R6) |
| 14 | Earn — deposit into product flow | Fake confirm + fake lock-up terms | Real deposit tx |
| 15 | Earn — withdraw/unstake flow | Fake unstake flow | Real unstake tx |
| 16 | Earn — active positions dashboard | Fake positions + fake accrued yield | Real position tracking |
| 17 | Fee transparency displays (P2P zero-fee, copy trading split) | Fake fee breakdown UI | Real fee calc |
| 18 | Theming events for copy/P2P/earn actions | Reuse `TradeExecuted`/custom events | Verify real triggers |
| 19 | Empty/error states across all three | Skeletons + error UI | Wire real error taxonomy |
| 20 | Phase QA + docs | Full walkthrough all three sub-features, update file | Full regression, update file |

---

### Topic 8 — Notifications System (D8 / R8)

**Goal:** Novu Cloud-backed multi-channel notifications (push, in-app,
email, AI-contextual alerts).

**Scope:** `android/feature-notifications`, backend Novu integration
(scaffolded D8, real R8).

**Phase acceptance (D8):** in-app notification feed UI, notification
settings screen, fake push notification triggers for every event type
listed in the blueprint — all fake, all themed.

**Phase acceptance (R8):** real Novu Cloud wiring — device token
registration, real triggers from backend events, real FCM push, real
email for security alerts.

| # | Slice | D-phase focus | R-phase focus |
|---|---|---|---|
| 1 | In-app notification feed — list UI | Fake notification list | Real Novu feed API |
| 2 | Notification detail / tap-through | Fake detail sheet + fake deep link | Real deep link to relevant screen |
| 3 | Notification settings screen (+ in-app sound toggle, see 3D) | Fake per-category toggles + sound on/off toggle | Real preference persistence via Novu |
| 4 | Push notification — trade execution | Fake local notification demo | Real FCM trigger from backend |
| 5 | Push notification — AI signal | Fake local notification demo | Real FCM trigger |
| 6 | Push notification — deposit/withdrawal | Fake local notification demo | Real FCM trigger |
| 7 | Push notification — price alerts | Fake local notification demo + fake alert-setup UI | Real AI-generated alert trigger |
| 8 | Push notification — security alerts | Fake local notification demo | Real trigger + real email channel |
| 9 | Device token registration flow (shell) | Fake "registering device" step in onboarding | Real Novu device registration |
| 10 | Notification badge/count sync | Fake badge count logic | Real unread-count sync |
| 11 | Swipe-to-dismiss / mark-read | Fake swipe actions | Real read-state persistence |
| 12 | Empty state (no notifications) | Fake empty illustration | — (verify only) |
| 13 | Marketing/promo notification card style | Fake promo card | Real promo campaign wiring |
| 14 | Email alert preview (security) | Fake email preview mockup | Real email template + send |
| 15 | Notification-triggered theming events | Verify every push maps to a `ThemeEvent` where applicable | Verify real end-to-end timing |
| 16 | Do-not-disturb / quiet hours setting | Fake toggle | Real scheduling logic |
| 17 | Notification grouping (by category) | Fake grouped sections | Real grouping logic |
| 18 | Backend Novu scaffold | Define trigger contracts, no real send | Real Novu Cloud account wiring |
| 19 | Accessibility pass | Screen reader labels for feed/settings | Verify with real content |
| 20 | Phase QA + docs | Full notification walkthrough, update file | Full regression, update file |

---

### Topic 9 — Security & Identity Layer (D9 / R9)

**Goal:** MPC key management, ZK-SNARK privacy, anti-phishing,
transaction validation, recovery.

**Scope:** `android/feature-security`, `services/backend` (security
modules).

**Phase acceptance (D9):** all security-related UI surfaces (MPC
key-share status, ZK privacy toggle, anti-phishing warning banners,
transaction confirmation modals, recovery setup — largely built
already in Topic 2 slices 13–14, cross-referenced here) — fake status
everywhere, fully themed.

**Phase acceptance (R9):** real MPC implementation, real ZK-SNARK
integration, real anti-phishing detection model, real multi-layer
transaction validation.

| # | Slice | D-phase focus | R-phase focus |
|---|---|---|---|
| 1 | Security dashboard / overview screen | Fake security score + checklist | Real security posture calc |
| 2 | MPC key-share status UI | Fake "key shares: 3/3 active" display | Real MPC key-share implementation |
| 3 | Hardware Keystore/TEE status indicator | Fake status badge | Real Keystore/TEE check |
| 4 | ZK-SNARK privacy toggle | Fake "private mode" toggle, fake explanation | Real ZK-SNARK transaction integration |
| 5 | Transaction validation modal (2FA+biometric+device) | Fake multi-step confirm modal | Real multi-layer validation |
| 6 | Anti-phishing warning banner | Fake warning trigger demo | Real AI-powered detection model |
| 7 | Anti-phishing — suspicious link/address check | Fake "checking address..." UI | Real real-time check |
| 8 | Certificate pinning status (dev-only diagnostic) | Fake diagnostic screen | Real pinning verification |
| 9 | Seed phrase backup flow (cross-ref Topic 2.13) | Reuse/extend fake flow | Real MPC-backed generation |
| 10 | Social recovery setup (cross-ref Topic 2.14) | Reuse/extend fake flow | Real recovery contract/mechanism |
| 11 | Social recovery — guardian management | Fake guardian list + fake invite | Real guardian invite/accept flow |
| 12 | Recovery execution flow (simulate lost device) | Fake "recover account" walkthrough | Real recovery execution |
| 13 | Session/device trust list (cross-ref Topic 2.15) | Reuse/extend fake list | Real trust list + real revoke |
| 14 | Biometric re-auth for sensitive screens | Fake re-auth gate on wallet/security screens | Real re-auth enforcement |
| 15 | Security notification triggers | Wire fake events to Topic 8's fake push | Real triggers |
| 16 | Anomaly detection alert UI | Fake "unusual activity" alert | Real AI anomaly scoring |
| 17 | Privacy settings screen (data/ZK controls) | Fake toggles | Real backing settings |
| 18 | Open-source/audit info screen | Fake "view source" links | Real repo links, real audit status |
| 19 | Theming events for security (ErrorOccurred etc.) | Verify fake triggers | Verify real triggers |
| 20 | Phase QA + docs | Full security walkthrough, update file | Full regression + security review notes, update file |

---

### Topic 10 — Theming Polish, Micro-interactions & Final QA (D10 / R10)

**Goal:** D10 = final cinematic polish pass across the entire dummy
app (this is where the "modern, cinematic, glass, every-huge-action-
triggers-something" requirement gets its dedicated audit). R10 = final
integration/performance/QA pass across the entire real app.

**Scope:** whole `android/` tree (audit, not new features).

**Phase acceptance (D10):** every screen built in D1–D9 audited against
Layer 1B tokens (no hardcoded colors), every major user action
confirmed to emit its `ThemeEvent` with both dark and light
expressions, micro-interactions (press states, transitions, haptics)
consistent app-wide, accessibility pass complete, retrospective written.

**Phase acceptance (R10):** full integration test across all real
services, performance pass, crash-resilience pass, final retrospective
— app is ready for closed beta.

| # | Slice | D-phase focus | R-phase focus |
|---|---|---|---|
| 1 | Cross-screen token audit — Dashboard/Trading | No hardcoded colors, tokens only | Real-data visual regression |
| 2 | Cross-screen token audit — Wallet/AI/Copy-P2P-Earn | Same | Same |
| 3 | Cross-screen token audit — Notifications/Security/Onboarding | Same | Same |
| 4 | ThemeEvent coverage audit | Every action → event, both modes, checklist in this file | Verify real trigger firing rates |
| 5 | Micro-interaction pass — press states | Reanimated-based press feedback everywhere | — (verify only) |
| 6 | Micro-interaction pass — screen transitions | Consistent transition curves | — (verify only) |
| 7 | Haptics pass | `expo`-equivalent/Android haptics on key actions | — (verify only) |
| 8 | Glass effect performance sanity check | Flag any obviously heavy blur usage (can't runtime-test) | Real on-device profiling |
| 9 | Dark mode full walkthrough | Screenshot every screen | Same, real data |
| 10 | Light mode full walkthrough | Screenshot every screen | Same, real data |
| 11 | Accessibility — text scaling | Full app pass | Verify with real content |
| 12 | Accessibility — screen reader labels | Full app pass | Verify with real content |
| 13 | Accessibility — contrast | Full app pass, both modes | Verify with real content |
| 14 | Empty/loading/error state consistency audit | Full app pass | Wire remaining gaps to real states |
| 15 | Icon & branding final pass | Finalize all placeholder assets | Confirm production assets in place |
| 16 | Dead code / mock-leftover cleanup | Flag (don't delete without confirmation) | Remove now-unused mock code (confirm first) |
| 17 | Integration test — full user journey (D: click-through, R: real) | Onboarding→Dashboard→Trade→Deposit→AI→Notify, all clickable | Same, end-to-end with real backend |
| 18 | Performance pass | N/A (flag for R10) | Image caching, list virtualization, recomposition audit |
| 19 | Crash resilience / error boundary pass | Fake error boundary UI | Real error boundary + logging |
| 20 | Final retrospective | Write `## Retrospective` section, what shipped, what's left | Write final retrospective, beta-readiness note |

---

## 5. QUICK REFERENCE

- Blueprint (product spec): `docs/TRADE_BLUEPRINT_v2.md`
- Theming engine spec: Blueprint Section 3B (Layer 1B)
- Tooltip + dynamic sound engine spec: Blueprint Section 3B.2 (`GlassTooltip`) + new Layer 1C, mapped in this file's Section 3D
- 10 phase-topics: Blueprint Section 9
- Session workflow: Section 1 of this file
- Status/next-slice: Section 2 of this file
- Per-phase scope + slice tables: Section 4 of this file

---

## 6. KNOWN ISSUES / NOTES FOR NEXT SESSION

*(Each session should append findings here — don't delete prior
entries, just add yours with your phase/slice and date.)*

- **[Doc creation]** This file and `TRADE_BLUEPRINT_v2.md` were
  generated from the original "TRADE" blueprint conversation, before
  any code exists and before the repo is created. Phase D1/Slice 1's
  first job is creating the repo per Section 0 and confirming the
  monorepo layout still makes sense once real tooling choices (DI
  framework, backend language: Ktor vs Spring Boot) are made — flag
  here if either changes.
- **[D1.S01 — 2026-08-18]** Repo created at
  `https://github.com/Zapier-codes/Trade`, initialized with
  `docs/HANDOVER.md` and `docs/TRADE_BLUEPRINT_v2.md` (patch #0001).
  Monorepo skeleton folders (`android/`, `services/trading-agents/`,
  `services/backend/`, `docs/`) not yet created — that's real
  Slice-1-scope work still open for the next session, which is
  Phase D1, Slice 2 per Section 2. Tooling choices (DI framework,
  Ktor vs Spring Boot) are still unmade — flag/decide in the session
  that builds the actual project scaffold.
- **[D1.S02 — 2026-08-18]** Clean Architecture skeleton added in
  `app/src/main/kotlin/com/trade/app/{domain,data,presentation}`:
  base `UseCase`/`NoParamsUseCase` contracts, a placeholder
  `AppBuildInfoRepository` interface + `GetAppBuildInfoUseCase`,
  `FakeAppBuildInfoRepository` (D-phase fake, canned response + fake
  `delay()`), `AppShellUiState` (Loading/Loaded), and
  `AppShellViewModel` wiring the chain together. `MainActivity` now
  consumes the ViewModel via `collectAsState()` instead of Slice 1b's
  hardcoded string. Added `android/ARCHITECTURE.md` documenting this
  convention for D2+ feature modules to follow.
  **Correction to the D1.S01b note above:** DI framework choice
  (Hilt vs Koin) is *not* Slice 2 scope — the Phase acceptance (R1)
  section is explicit that this is finalized in R1, not D1. Slice 2
  therefore uses manual wiring only: `AppContainer.kt` (temporary,
  explicitly labeled, deleted when R1 wires real DI). Future feature
  modules should give themselves their own equivalent temporary
  container rather than routing through `AppContainer` — see
  `android/ARCHITECTURE.md`.
- **[CI addition — 2026-08-18, explicit human request, ahead of R1]**
  Added `.github/workflows/ci.yml`. This is normally R1 scope ("Verify/
  finalize build config, CI hooks" — see Topic 1's Slice 1b row), but
  the human explicitly asked for it now, so it's done ahead of
  schedule rather than deferred. Does **not** count as any D1 slice
  being complete — slice table/status table are unchanged by this
  entry. Behavior: runs on every push/PR touching `android/**`,
  `./gradlew check` only (compile + unit tests + lint, debug variant) —
  no `assemble`/`bundle`/`assembleRelease`, no artifact upload, since
  there's no signing config and shouldn't be one yet. Build is stamped
  `TRADE-0.1.<github.run_number>` via `-PappVersionCode`/
  `-PappVersionName` (now read from project properties in
  `android/app/build.gradle.kts`, falls back to `TRADE-0.1.0-dummy`
  for local/manual builds). Caching: `org.gradle.caching=true` +
  `org.gradle.parallel=true` in `gradle.properties`, plus
  `gradle/actions/setup-gradle@v4` persisting the Gradle Home (deps,
  wrapper, local build cache dir) across CI runs via GitHub's cache
  backend — unchanged modules come back FROM-CACHE/UP-TO-DATE instead
  of re-running, so a module that already passed doesn't get re-run
  (or newly fail) because an unrelated module changed. Real R1 work
  when that phase arrives: finalize whether this needs a real device/
  emulator instrumented-test job, decide demo/live build variants
  (Slice 15) before CI needs to build them, and revisit once signing
  config exists (must stay untouched by CI until then per the
  non-negotiable rules).
- **[D1.S01b — 2026-08-18]** Gradle scaffold added: root
  `settings.gradle.kts`/`build.gradle.kts`, four modules (`app`,
  `core-theme`, `core-ui`, `core-navigation`) each with a minimal
  `build.gradle.kts` and a placeholder Kotlin file noting which future
  slice owns their real content. `app` has `TradeApplication`,
  `MainActivity` (renders one unstyled placeholder screen — no
  navigation graph yet, that's Slice 3), and the Pawns/Consent scaffold
  from the Section 3C addendum: `PawnsManager` (fake in-memory consent,
  no real SDK/API key), `ConsentModal` (4 tabs: General/Privacy/Data
  Protection/Data Sharing, plain Material3 — reskin onto Glass
  primitives after Slices 7-9 exist, not before), and
  `PawnsBootReceiver` (manifest-registered, no-op until R-phase).
  **Gap:** no Gradle wrapper — this sandbox has no network access to
  generate one; see `android/gradle/README.md`. Human needs to run
  `gradle wrapper --gradle-version 8.9` locally and commit the result
  before this will actually build. DI framework choice (Hilt vs Koin)
  still open — deferred to Slice 2 as planned.
- **[Doc creation]** Session partitioning (Section 1) intentionally
  does *not* pre-assign exact files per slice the way a normal
  single-repo phase doc would, because the repo doesn't exist yet.
  The first several sessions in D1 should be more precise in their own
  handoff notes about exact file paths created, so later sessions in
  D2+ have real paths to reference (the way the reference project's
  Section 3A "re-audit" corrected assumptions after real code existed).
- **[D1.S03 — 2026-08-18]** Navigation graph added in
  `core-navigation/src/main/kotlin/com/trade/core/navigation/`:
  - `TradeRoute.kt` — every planned route (63 destinations) registered
    as `TradeRoute(path, title, group, slice)` data, one entry per
    genuinely distinct navigable destination across Topics 2-9's slice
    tables (fake sub-states of one screen, e.g. deposit success vs.
    fail, stay in-screen — not separate routes). `TradeRoutes.all` is
    the single source of truth `TradeNavHost` reads from.
  - `EmptyRouteScreen.kt` — the one shared placeholder every route
    resolves to, showing route title/group/owning slice and a "back to
    directory" button. Deliberately plain Material3 (no `core-theme`
    tokens/`core-ui` glass primitives — Slices 4-9 don't exist yet).
  - `RouteDirectoryScreen.kt` — new start destination (`"directory"`),
    not a product screen: a flat, grouped, tappable list of every
    route, added specifically so the human can reach any of the 63
    placeholder screens on a real device for review without deep-link
    tooling (Section 3's "phone/Termux workflow" concern). Real D2
    app-entry routing (welcome vs. returning-user auth vs. dashboard)
    is explicitly *not* decided here — flagging per Section 1 Rule 3
    ("never re-architect... without saying so") in case D2 expects
    `TradeNavHost`'s start destination itself, not just its content.
  - `TradeNavHost.kt` — wires directory + all 63 routes; takes
    Slice 2's `AppShellViewModel` build-info string as a subtitle
    parameter so that chain stays live instead of being orphaned by
    the new host.
  - `MainActivity.kt` updated: renders `TradeNavHost` instead of
    Slice 2's single hardcoded screen; `AppShellViewModel` wiring
    unchanged, its state now feeds the directory subtitle.
  - `core-navigation/build.gradle.kts`: added `foundation` (LazyColumn/
    clickable) and `material3` deps, needed for the two new screens.
  - Removed `core-navigation`'s Slice-1b `Placeholder.kt` stub (real
    content now present).
  - **Convention for D2+ (documented at top of `TradeRoute.kt`):** when
    a feature module ships a route's real screen, point `TradeNavHost`
    at it and drop that route from ad-hoc placeholder use — don't
    duplicate route ids elsewhere.
  - Not independently buildable in this sandbox (no Gradle wrapper
    yet, per the D1.S01b gap note above) — reviewed by hand for
    import/package correctness only, same caveat as prior slices.
- **[D1.S04 — 2026-08-18]** `TradeThemeDark` full color token set added
  in `core-theme/src/main/kotlin/com/trade/core/theme/`:
  - `TradeColorTokens.kt` — the Color token group's *shape* (data
    class), shared by `TradeThemeDark` (this slice) and `TradeThemeLight`
    (next slice). Broader than the blueprint's illustrative example list
    (`surface`/`surfaceGlass`/`accentPrimary`/`accentSignal`/
    `ambientGlow` — those five kept, marked in comments) — added the
    background/text/divider basics no screen ships without, plus
    `positive`/`negative`/`depositGold`/`warning`/`error` semantic
    tokens implied by the fee/result display rules in Blueprint 10.1
    and the `ThemeEvent` table in 3B.3.
  - `TradeThemeDark.kt` — the actual dark-mode values: near-black base,
    cool-leaning neutral ramp, translucent glass fill + cool-white
    border stroke per Blueprint 3B.2's dark-glass description, gold
    reserved specifically for `depositGold` (matching `DepositConfirmed`'s
    "gold ambient sweep," Blueprint 3B.3) rather than used generally.
  - Removed `core-theme`'s Slice-1b `Placeholder.kt` stub.
  - **Deliberately NOT in this slice** (flagging per Section 1 Rule 3
    rather than guessing ahead): no `LocalTradeTheme` `CompositionLocal`
    and no `TradeTheme { }` wrapper Composable yet. Blueprint 3B.1 says
    screens should read `LocalTradeTheme.current.X` and never branch on
    light/dark, but wiring that requires `TradeThemeLight` to exist too
    (Slice 5) — building the switch mechanism around only one of the two
    token sets risked guessing its shape wrong. Whoever does Slice 5
    should either add the CompositionLocal/wrapper as part of that slice
    or explicitly defer it to Slice 6 (Typography + spacing) — your call,
    just don't let it silently slip past both.
  - Not independently buildable in this sandbox (no Gradle wrapper),
    reviewed by hand only, same caveat as prior slices.
- **[Doc addendum, explicit human request, ahead of Slice 5 — 2026-08-18]**
  Added Section 3D (this file) + a new Blueprint "Layer 1C: Dynamic
  Notification & Action Sound Engine" section + a new `GlassTooltip`
  entry in Blueprint 3B.2, per explicit human request. **Documentation
  only — no code in this session, no slice status changed.** Human
  confirmed: (1) "pro tools" meant "whatever solid production-grade
  Gradle libraries fit," not a named library — resolved to: no new
  Gradle dependency needed for either feature (`GlassTooltip` builds on
  Material3's existing `TooltipBox`/`PlainTooltip`/`RichTooltip`;
  `SoundReactor` uses native `android.media.SoundPool`, not a
  third-party audio lib) — flag in a future session if that changes;
  (2) sound engine should mirror `ThemeEventBus`/`ThemeReactor`
  architecturally, confirmed: `SoundReactor` subscribes to the *same*
  bus, no second event system. Slice-table changes: Topic 1 Slice 9 now
  includes `GlassTooltip`, Topic 1 Slices 10-11 now include
  `SoundTokens`/`SoundReactor` scaffolding, Topic 8 Slice 3 now includes
  an in-app sound on/off toggle. None of these slices are built yet
  (Slice 5 is still next) — this only changes what those *future*
  sessions' scope includes when they're reached. See Section 3D for the
  full mapping and non-negotiable rules (reuse the one event bus; every
  new `ThemeEvent` needs a matching `SoundTokens` entry going forward).
- **[Doc correction — 2026-08-18]** Section 1's `git am` apply-command
  example used `~/Downloads/<patch>` — wrong for this project's actual
  device workflow, which is Termux, where the real Downloads folder is
  linked at `~/storage/downloads/` (after `termux-setup-storage` has
  been run once). Corrected in Section 1 step 7. **Every prior
  session's handoff message that told the human to run
  `git am ~/Downloads/...` should be read as `git am
  ~/storage/downloads/...` instead** — the patch files themselves are
  unaffected, only the path in the apply command was wrong.

---

*End of HANDOVER.md. Next session: Category 1, Phase D1, Slice 5 —
Color tokens (light).*
