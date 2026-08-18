# services/backend

Reserved directory — API gateway + services (Ktor or Spring Boot,
PostgreSQL, Redis, RabbitMQ/Kafka; final choice is R1 scope). See
`docs/TRADE_BLUEPRINT_v2.md` Section 3 for the architecture.

Also the eventual home of the `FeeEngine` service (net_stake calculation,
bonus formula, withdrawable-vs-bonus balance split — see
`docs/HANDOVER.md` Section 3C addendum). R1 scaffolds its interface;
R4/R5 wire trade settlement and deposit bonus crediting into it.

Nothing lives here yet. Do not add code here before R1 unless a slice
explicitly calls for it.
