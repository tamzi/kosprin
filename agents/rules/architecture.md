# Architecture rules

These constrain *how* code is added across services. The full architectural picture lives in [`docs/tech/systemdesign.md`](../../docs/tech/systemdesign.md); this file lists the rules that flow from it.

## Module boundaries
- A service module owns its data store. No service reaches into another service's database directly — cross-service reads go through the owning service's API or a Kafka topic.
- `common/` is **not** a dumping ground. It holds: error envelopes, event base types, MDC/tracing helpers, shared autoconfig. Domain types belong to the service that owns them.
- The `gateway` module has no business logic. Routing, auth, rate-limiting, CORS — that's it.

## Sync vs async
- Client-facing reads are synchronous REST through the gateway.
- Anything that can be eventually consistent (search index, feed fan-out, notifications, analytics) goes via Kafka.
- A service must not call another service synchronously to satisfy an async-eligible workflow.

## Idempotency
- Every Kafka message carries an `eventId` (UUID v7). Consumers dedupe.
- Every state-changing HTTP endpoint either is naturally idempotent or accepts an `Idempotency-Key` header.

## Data
- All timestamps stored in UTC.
- Schema migrations: Flyway, forward-only. Never edit a previously-applied migration.
- Cache writes never replace the system of record. Caches are read-through, source-of-truth lives in Postgres / ES.

## Auth
- Services validate JWTs against Keycloak. They do not parse roles from arbitrary headers.
- Service-to-service: client-credentials flow when system-initiated, forwarded user token when on behalf of a user.

## Observability
- Every service exposes `/actuator/health`, `/actuator/prometheus`, `/v3/api-docs`.
- Logs are JSON. Always include `traceId`, `spanId`, `service`. Never log secrets or PII.
