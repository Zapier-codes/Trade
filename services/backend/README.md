# services/backend

Reserved directory — API gateway + services (Ktor or Spring Boot,
PostgreSQL, Redis, RabbitMQ/Kafka; final choice is R1 scope). See
`docs/TRADE_BLUEPRINT_v2.md` Section 3 for the architecture.

Also the eventual home of:
- The `FeeEngine` service (net_stake calculation, bonus formula,
  withdrawable-vs-bonus balance split — see `docs/HANDOVER.md`
  Section 3C addendum). R1 scaffolds its interface; R4/R5 wire trade
  settlement and deposit bonus crediting into it.
- A Binance execution adapter (order routing via
  `binance-connector-java`, maker/taker fee handling for
  Binance-routed orders only — see `docs/HANDOVER.md` Section 3F
  addendum and `docs/TRADE_BLUEPRINT_v2.md` Section 10.1a). R1
  scaffolds its interface; R4 wires real order routing into it. Do
  not start this before Section 3F's flagged open question
  (absorb-vs-pass-through on the Binance fee) is answered.

Nothing lives here yet. Do not add code here before R1 unless a slice
explicitly calls for it.
