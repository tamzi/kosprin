---
name: modulesLayout
description: Multi-module Gradle layout — common + 6 services + gateway + dashboard.
type: memory
---

# Modules layout

Gradle multi-module workspace. `settings.gradle.kts` is the source of truth for the JVM modules; the dashboard is a separate npm project at the root.

| Module                  | Role                                                                  |
|-------------------------|-----------------------------------------------------------------------|
| `common`                | Shared error envelopes, event base types, MDC helpers, autoconfig.    |
| `gateway`               | Routing, auth, rate-limiting, CORS. **No business logic.**            |
| `metadata-service`      | Video / user metadata, owner of the metadata DB.                      |
| `feed-service`          | Personalised feed; consumes events from other services.               |
| `video-service`         | Video upload / processing pipeline; producer of `videos`, `video-processed`. |
| `search-service`        | Elasticsearch read model; consumes events for indexing.               |
| `notification-service`  | Push / email notifications; consumes `notifications`.                 |
| `analytics-service`     | Event aggregation; consumes `analytics`.                              |
| `dashboard/`            | TypeScript / React frontend. Separate npm project, not in `settings.gradle.kts`. |

**Why:** each service module owns its data store. `common/` is intentionally narrow — domain types belong to the service that owns them, and the `gateway` is deliberately stripped of business logic so it stays cheap to scale and easy to reason about.

**How to apply:** when adding a new feature, decide which module owns it before writing code. Cross-service reads go through the owning service's API or a Kafka topic, never directly into another service's database. See [`../rules/architecture.md`](../rules/architecture.md).
