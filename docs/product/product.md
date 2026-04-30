# Product Overview

## What is kosprin
A Kotlin/Spring-Boot template for a small-to-mid-scale distributed system, structured as a mini YouTube/Instagram-style backend. The repo's purpose is to act as a **runnable reference** that real services can be forked from — every architectural concern (gateway, IAM, async messaging, search, cache, analytics, observability) is represented end-to-end so a team building a new product doesn't have to re-derive the wiring.

## Audience
- Backend engineers bootstrapping a microservices product.
- Engineers learning Spring Cloud + Kafka without trawling tutorials.
- Architects validating a service decomposition before greenfield work.

## Product surface
The system exposes three user-facing surfaces:

| Surface       | Audience          | Owner              |
|---------------|-------------------|--------------------|
| REST API      | Mobile/web client | Gateway → services |
| Realtime feed | End user          | Feed service       |
| Dashboard     | Operator/analyst  | Analytics + UI     |

## Core capabilities (target state)
1. **Content lifecycle** — upload metadata, async processing, search index update, fan-out to feeds.
2. **Discovery** — full-text search with caching for hot queries.
3. **Engagement** — personalised feed, notifications.
4. **Insight** — analytics rollups visualised in the dashboard.
5. **Operability** — metrics, traces, logs, health checks across every service.

## Out of scope (for now)
- Real video transcoding (simulated; the abstraction is in place for ffmpeg later).
- Mobile or web client UI beyond the analytics dashboard.
- Production CDN — represented by Nginx in dev.
- Multi-tenancy.

## Roadmap
See [`roadmap.md`](roadmap.md) for the phased implementation plan that maps to the Jira board.
