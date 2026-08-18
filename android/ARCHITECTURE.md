# TRADE — Architecture convention (established D1/Slice 2)

Every feature, starting with D2's `feature-onboarding`/`feature-auth`,
should follow the same three-layer shape demonstrated in
`app/src/main/kotlin/com/trade/app/{domain,data,presentation}`:

- **`domain/`** — pure Kotlin. Models, repository *interfaces*
  (never implementations), and use cases implementing [`UseCase`]/
  [`NoParamsUseCase`] (`app/.../domain/UseCase.kt`). No Android
  imports, no knowledge of D-phase vs R-phase.
- **`data/`** — implements the `domain/` repository interfaces.
  D-phase implementations are fakes: canned responses, optional
  `delay()` to simulate latency, no network/real calls (see
  `FakeAppBuildInfoRepository`). R-phase swaps the implementation;
  the `domain/` interface and use case never change.
- **`presentation/`** — ViewModels exposing a sealed `UiState`
  (`Loading` / `Loaded` / ... — see `AppShellUiState`) via
  `StateFlow`, consumed from Compose with `collectAsState()`.

**DI is not part of this convention yet.** DI framework choice
(Hilt vs Koin) is explicit R1 phase-acceptance scope — see
`docs/HANDOVER.md` "Phase acceptance (R1)". Until then, wire fakes
manually the way `AppContainer.kt` does for the app module, and give
each feature module its own equivalent temporary container rather
than routing everything through `AppContainer`. When R1 lands, these
manual containers get deleted and replaced by real DI modules — that
migration touches every feature, so keep the manual wiring in one
small, obviously-temporary file per module rather than spreading it
around.
