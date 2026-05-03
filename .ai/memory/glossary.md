---
name: glossary
description: Domain terms and acronyms — KOS, sacrament, DLT, STRIDE, Outcome, UUID v7, …
type: memory
---

# Glossary

Domain terms and acronyms an assistant cannot infer from code. Keep entries short — one to two sentences plus a link if there's a longer explanation elsewhere.

Format: term — definition. Alphabetised.

---

- **ADR** — Architecture Decision Record. One Markdown file per non-trivial, hard-to-reverse decision. Lives under `docs/adr/`. See [writeAdr](../skills/writeAdr.md).
- **DLT** — Dead-Letter Topic. Where a Kafka consumer routes poison messages it can't process. Required by the [architecture rules](../rules/architecture.md).
- **Idempotency-Key** — HTTP header on state-changing endpoints; the consumer dedupes on this. Pairs with `eventId` for Kafka.
- **KOS-X.Y** — Ticket ID convention. `KOS-X` is an epic in [`/jiraboard.md`](../../jiraboard.md); `KOS-X.Y` is a sub-task. Sequential per epic; never reused.
- **kosprin** — Project name. Mini YouTube/Instagram backend in Kotlin/Spring-Boot, used as a teaching template.
- **Outbox pattern** — Tracked under KOS-21. Pattern for publishing Kafka events reliably from a service that also writes to Postgres in the same transaction.
- **Outcome\<T\>** — Service-layer result type from `common/`. Used instead of throwing for control flow. See [codeStyle](../rules/codeStyle.md).
- **STRIDE** — Threat-modelling categories: Spoofing, Tampering, Repudiation, Information disclosure, Denial of service, Elevation of privilege. Used in [threatModelFeature](../skills/threatModelFeature.md).
- **UUID v7** — Time-ordered UUID variant. Used for `eventId` on every Kafka message because lexicographic sort = chronological sort.
