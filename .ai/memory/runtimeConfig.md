---
name: runtimeConfig
description: Service ports, infra ports, and the standard env-var contract.
type: memory
---

# Runtime config

Service ports are deliberately stable so dashboards, hosts file entries, and Kafka client configs do not drift.

| Port  | Service / infra              |
|-------|------------------------------|
| 8080  | Keycloak (dev)               |
| 5432  | Postgres                     |
| 6379  | Redis                        |
| 9092  | Kafka (broker, in-cluster)   |
| 29092 | Kafka (broker, host-exposed) |
| 9200  | Elasticsearch                |

Application service ports (gateway, metadata, feed, video, search, notification, analytics) are not yet finalised — use the next available `818X` slot per service when wiring them. Promote to this table once stable.

Standard env-var contract: app-specific variables use the `KOSPRIN_` prefix in `SCREAMING_SNAKE_CASE`. Infra credentials follow vendor conventions (`POSTGRES_PASSWORD`, `KEYCLOAK_ADMIN_PASSWORD`). Secrets are **never** committed; `.env` examples use placeholders (`changeme`, `XXXX`) — see [`pushPolicy.md`](pushPolicy.md) and [`../rules/agentBehaviour.md`](../rules/agentBehaviour.md).

**Why:** the local stack in `docker-compose.yml` pins these ports; deviating in code means every developer rebuilds their muscle memory. The `KOSPRIN_` prefix protects against env-var collisions when running multiple services side by side.

**How to apply:** when wiring a new service, read this file first; when changing a port, update this file and the docker-compose entry in the same commit.
