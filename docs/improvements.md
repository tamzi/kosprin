# Improvement Ideas

A backlog of "beyond baseline" improvements. Each one is optional — they are not required for the system to function, but they materially raise quality, reliability, or developer velocity. Higher-leverage items should graduate to the [Jira board](../jiraboard.md) under `KOS-S*`.

## Build & developer experience
- **Version catalog** (`gradle/libs.versions.toml`) — one source of truth for dependency versions; eliminates the brittle `ext[...]` map.
- **Convention plugins** under `buildLogic/` — turn each service module's build into a 3-line file.
- **Generated TypeScript clients** for the dashboard, produced from each service's OpenAPI spec at build time. Removes hand-rolled `axios` boilerplate and stops typing drift.
- **Pre-commit + commit-msg hooks** mirroring the parent CLAUDE.md project conventions (atomic commits, past-tense messages).
- **Renovate or Dependabot** for dependency PRs.

## Reliability & data integrity
- **Outbox pattern** for Kafka publishing from services that also write to Postgres. Removes the "did the DB write commit but the Kafka send fail?" gap.
- **Schema registry (Avro/Protobuf)** with Confluent or Apicurio. Replaces JSON-by-convention; CI gates incompatible changes.
- **Idempotency keys** at the gateway for create operations (e.g. `Idempotency-Key` header) so a client retry doesn't double-create.
- **Saga / orchestration** for multi-service workflows (upload → process → notify). Today there is no compensation path if a stage fails halfway.
- **Dead-letter dashboards** in Grafana — visible queue depth per DLT, not just metrics in Prometheus.

## Performance
- **Multi-tier cache** (caffeine in-process + Redis shared). Avoids hammering Redis for hottest keys.
- **CQRS read models for analytics** — denormalised tables / materialized views rebuilt from the `analytics` topic, separate from the OLTP path.
- **gRPC for internal RPC** — keep REST/JSON at the edge, move service-to-service calls to gRPC for lower latency and codegen contracts.
- **HTTP/2 (or HTTP/3) at the gateway** for client-facing endpoints.
- **Read replicas** for the metadata DB once read traffic dominates writes.

## Observability
- **OpenTelemetry traces** (KOS-13.5) — already on the board. Adding it: Tempo backend + Grafana for traces alongside metrics.
- **Loki for logs** — keeps logs queryable from the same Grafana instance as metrics/traces.
- **SLOs per service** with error-budget burn-rate alerts, replacing simple threshold alerts.
- **Continuous profiling** (Pyroscope) — find hot methods without a JFR session.

## Security
- **Service mesh (Istio or Linkerd)** for mTLS, retries, and policy without code changes.
- **Per-tenant rate limits** at the gateway.
- **Signed Kafka events** for audit trails on regulated data.
- **Image signing** (cosign) and admission-controller verification.
- **Zero-trust ingress** with WAF in front of the gateway.

## Operability
- **Feature flags** (Unleash, PostHog, or LaunchDarkly) to gate risky rollouts. Ties cleanly into a canary deployment story.
- **Argo Rollouts or Flagger** for canary/blue-green release strategies.
- **Chaos testing** (LitmusChaos or Chaos Mesh) — fault injection in a sandbox cluster.
- **Backup & restore drills** for Postgres and Elasticsearch on a schedule.

## Documentation
- **Architecture Decision Records** under `docs/adr/` for significant choices (Keycloak over custom auth, Kafka over RabbitMQ, etc.).
- **Module READMEs** (< 100 lines each) per the project doc rules — link to the deeper docs in `docs/tech/`.
- **Runbooks** under `docs/runbooks/` — one per alert.
- **Onboarding script** that exercises the docs end-to-end.

## Quick wins (under a day each)
1. [x] Fix the `loadbalancer.md` vs `LoadBalancer.md` link inconsistency in [`tutorial.md`](tutorial.md).
2. [x] Add `scripts` to [`dashboard/package.json`](../dashboard/package.json).
3. [x] Add a `docs/README.md` index.
4. [x] Convert the root `ext[...]` block to a version catalog.
5. [x] Add `apply false` to the root `build.gradle.kts` plugins.

(All five quick wins landed alongside the agent-agnostic rules layout. The remaining items in the sections above are still open.)
