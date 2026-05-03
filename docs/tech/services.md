# Services

One-page summary of every backend module. Cross-references the [system design](systemdesign.md).

| Module                  | Type           | Stores              | Kafka (in)                   | Kafka (out)                   |
|-------------------------|----------------|---------------------|------------------------------|-------------------------------|
| `gateway`               | Spring Cloud Gateway | —              | —                            | —                             |
| `common`                | Library        | —                   | —                            | —                             |
| `metadata-service`      | REST + Kafka   | Postgres + Redis    | `video-processed`            | `videos`                      |
| `video-service`         | Worker         | Object storage      | `videos`                     | `video-processed`             |
| `search-service`        | REST + Kafka   | Elasticsearch + Redis | `videos`                   | `analytics`                   |
| `feed-service`          | REST + Kafka   | Postgres            | `videos`                     | `notifications`, `analytics`  |
| `notification-service`  | Worker         | Postgres / Redis    | `notifications`              | (DLT)                         |
| `analytics-service`     | REST + Kafka   | Postgres            | `analytics`                  | —                             |

## gateway
Single ingress for all clients. Validates JWTs, enforces rate limits, applies CORS, and routes to downstream services using Spring Cloud LoadBalancer. Owns no business logic.

## common
Shared library: error envelopes, event base types, MDC/trace propagation, common autoconfig. Versioned alongside the services; consumed via `implementation(project(":common"))`.

## metadata-service
System-of-record for content metadata. Owns the `videoapp_db` Postgres schema. Reads are cached in Redis; writes publish a `videos` event for downstream services. Listens for `video-processed` to update processing status on the same row.

## video-service
Stateless worker. Consumes `videos`, simulates processing (thumbnail + duration), writes outputs to object storage, and emits `video-processed`. Designed to scale horizontally — partition key is the video ID so all events for one video land on one consumer.

## search-service
Maintains the Elasticsearch `videos` index. Consumes `videos` to upsert documents and exposes a search REST API with Redis-cached hot queries. Emits search-volume events to the `analytics` topic.

## feed-service
Builds personalised feeds. Consumes `videos`, fans out to follower feeds in `feed_db`, and emits `notifications` and `analytics` events.

## notification-service
Consumes `notifications` and dispatches via pluggable channels (log/email/in-app). Owns retry and dead-letter handling.

## analytics-service
Consumes `analytics` events, rolls up counters into `analytics_db`, and exposes REST endpoints to feed the dashboard.

## Status
All modules are scaffolded but **not implemented** — see [`/jiraboard.md`](../../jiraboard.md). Per-module READMEs will be added under each module directory once the foundation epic (KOS-1) lands.
