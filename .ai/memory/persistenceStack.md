---
name: persistenceStack
description: Postgres per service + Elasticsearch for search + Redis for cache; Flyway forward-only migrations.
type: memory
---

# Persistence stack

Each service module owns its own logical Postgres database (single physical instance for now, separated by schema or DB name). Read-only projections that need full-text or aggregate search live in Elasticsearch. Redis is used as a read-through cache only — it never replaces the system of record.

- **Postgres 16** — system of record for every service that has state.
- **Elasticsearch 8.13** — read model for `search-service`. Source-of-truth always lives in the producing service's Postgres.
- **Redis 7** — cache layer; writes go to Postgres first, cache is invalidated on the relevant Kafka event.

Schema migrations: **Flyway, forward-only**. Once a migration is applied to any environment, it is never edited — superseded by a new migration. All timestamps are stored in **UTC**.

**Why:** "owned database per service" prevents cross-service direct reads, which would couple deploy cycles. Flyway forward-only is non-negotiable because rolling back a migration in prod is far more expensive than rolling forward with a corrective one. Cache-as-source-of-truth is the most common production pain we want to design out from day one.

**How to apply:** when adding a column or table, write a Flyway migration via [`../skills/addDbMigration.md`](../skills/addDbMigration.md). When adding a search-backed query, project from Postgres into ES via the consumer in `search-service` — never write directly to ES from the producing service.
