# Kosprin Jira Board

Faux project key: **KOS**. Status legend: `[ ]` to do · `[~]` in progress · `[x]` done · `[!]` blocked.

This board is the **single source of truth** for project work. Every improvement, fix, or enhancement is tracked here. Work the priority bands top-down — P0 lands before P1, and so on. Items inside a band can run in parallel.

The old `docs/improvements.md` backlog was consolidated into this file; the standalone improvements doc has been removed.

## Working agreement
- **Finish all stories in an epic before starting a different epic.** Don't cherry-pick.
- **Within an epic, complete the current story end-to-end before opening another.**
- If a story is genuinely **blocked** by a dependency, mark it `[~]` (in progress, parked) or `[!]` (blocked) with a one-line reason in the bullet, then continue with the rest of the same epic. Never silently skip a story to jump elsewhere.
- **Stretch items in P9 do not start until** there is real bandwidth, or until one of them unlocks higher-priority work — at which point promote it into the band where it belongs.

This rule is also captured in [`agents/rules/agentBehaviour.md`](agents/rules/agentBehaviour.md) so every AI assistant in the repo follows it.

---

# P0 — Foundation
Unblocks every other band. Until P0 is done, services cannot start cleanly and there is no local stack to run them against.

## KOS-1 — Build System Foundation
- [x] KOS-1.1 — Fix root [`build.gradle.kts`](build.gradle.kts): plugins declared with `apply false`, applied per submodule.
- [x] KOS-1.2 — Replace `ext[...]` version pinning with a `gradle/libs.versions.toml` version catalog.
- [x] KOS-1.3 — `buildLogic/` convention plugins: `kosprin.kotlin-library`, `kosprin.spring-service`, `kosprin.kafka-consumer`.
- [x] KOS-1.4 — Populate every empty submodule `build.gradle.kts`.
- [x] KOS-1.5 — Detekt + ktlint + JaCoCo wired through convention plugins; ben-manes versions plugin at root for `dependencyUpdates`.
- [x] KOS-1.6 — Add `dependencyResolutionManagement` block to `settings.gradle.kts`.

## KOS-2 — Common Module
- [x] KOS-2.1 — Define error envelope types (`ApiError`, `ProblemDetail` per RFC 7807).
- [x] KOS-2.2 — Define shared event base types and Kafka headers (correlation ID, source, schema version).
- [x] KOS-2.3 — `Result<T>` / `Outcome<T>` helper for service-layer returns.
- [x] KOS-2.4 — Global exception handler + `@ControllerAdvice` autoconfig.
- [x] KOS-2.5 — Tracing/MDC filter for correlation-ID propagation across HTTP and Kafka.

## KOS-3 — Local Development Infrastructure
- [x] KOS-3.1 — `docker-compose.yml` with PostgreSQL, Redis, Kafka (KRaft mode), Elasticsearch, Keycloak.
- [!] KOS-3.2 — Per-service `Dockerfile` (multi-stage, JLink runtime image). Blocked — services have no source code yet; Dockerfiles need a compiled jar to package.
- [~] KOS-3.3 — Postgres init scripts checked in under `infra/postgres-init/`. Keycloak realm export pending KOS-5 design.
- [x] KOS-3.4 — Kafka topic auto-create via `kafka-init` one-shot container in compose.
- [x] KOS-3.5 — `Makefile` for common dev tasks (`up`, `down`, `reset`, `logs`, `psql`, `redis-cli`, `kafka-topics`, `build`, `test`).

## KOS-14 — Tests (unit, integration, E2E)
Moved into P0 — services land with tests, not retrofitted later.

- [x] KOS-14.1 — Unit-test scaffolding per module: **JUnit 6** (BOM in convention plugin), MockK, Kotest assertions.
- [~] KOS-14.2 — Integration tests with Testcontainers (Postgres, Kafka, Redis, ES) under `src/integrationTest/kotlin`. Source set + deps wired; real container tests blocked until services have DB/Kafka logic.
- [ ] KOS-14.3 — Contract tests between producers/consumers (Spring Cloud Contract or Pact).
- [~] KOS-14.4 — GitHub Actions: `build`, `test`, `detekt`, `ktlint`, `dependencyUpdates`, image build & push. CI runs check + dependencyUpdates; image build & push step missing — blocked on KOS-3.2 Dockerfiles.
- [x] KOS-14.5 — Coverage gate (JaCoCo, ≥70% line, ≥60% branch).
- [ ] KOS-14.6 — End-to-end tests against the full local stack (Docker Compose) — one happy-path + one failure path per business workflow (upload → process → search → feed → notify).

---

# P1 — Edge & Identity
Lets a JWT-authenticated client reach a service through the gateway.

## KOS-4 — API Gateway
- [ ] KOS-4.1 — Route table for all 6 services (path predicates + load-balanced URIs).
- [ ] KOS-4.2 — Keycloak OIDC integration; JWT validation filter.
- [ ] KOS-4.3 — Per-route rate limiter backed by Redis (incl. per-tenant variant).
- [ ] KOS-4.4 — Global CORS, request logging, and correlation-ID injection filters.
- [ ] KOS-4.5 — `/actuator/health` aggregation across downstream services.
- [ ] KOS-4.6 — Circuit breaker + retry policies (Resilience4j).
- [ ] KOS-4.7 — `Idempotency-Key` header support for create operations.

## KOS-5 — Identity & Access (Keycloak)
- [ ] KOS-5.1 — Realm export `kosprin-realm.json` with `kosprin-gateway`, `kosprin-services` clients.
- [ ] KOS-5.2 — Roles: `user`, `creator`, `moderator`, `admin`.
- [ ] KOS-5.3 — Resource-server config in each service to validate JWT and map roles to authorities.
- [ ] KOS-5.4 — Client-credentials flow for service-to-service calls.

---

# P2 — Core write path
The system can ingest content and process it asynchronously.

## KOS-6 — Metadata Service
- [ ] KOS-6.1 — Domain model: `Video`, `User`, `Tag`. JPA entities + Flyway migrations.
- [ ] KOS-6.2 — REST endpoints: create/read/update/list metadata.
- [ ] KOS-6.3 — Redis read-through cache for hot paths (`GET /videos/{id}`, list-by-user).
- [ ] KOS-6.4 — Kafka producer for `videos` topic on create.
- [ ] KOS-6.5 — Kafka consumer for `video-processed` topic to update processing status.
- [ ] KOS-6.6 — OpenAPI spec published at `/v3/api-docs`.

## KOS-7 — Video Processing Service
- [ ] KOS-7.1 — Kafka consumer for `videos` topic.
- [ ] KOS-7.2 — Simulated processing pipeline (sleep + log; pluggable handler interface for future ffmpeg).
- [ ] KOS-7.3 — Object-storage abstraction (local FS by default, S3-compatible adapter later).
- [ ] KOS-7.4 — Kafka producer for `video-processed` topic with thumbnail URL + duration.
- [ ] KOS-7.5 — Idempotent consumer (dedupe by `eventId`).

---

# P3 — Read paths
Clients can search and consume feeds.

## KOS-8 — Search Service
- [ ] KOS-8.1 — Elasticsearch index template for `videos` (analyzer + mapping).
- [ ] KOS-8.2 — Kafka consumer for `videos` topic → index documents.
- [ ] KOS-8.3 — Search REST endpoint with pagination, faceting on tags.
- [ ] KOS-8.4 — Redis cache for popular query strings (TTL ~60s).
- [ ] KOS-8.5 — Reindex CLI / admin endpoint.

## KOS-9 — Feed Service
- [ ] KOS-9.1 — JPA model for `FeedItem`, `Follow` graph.
- [ ] KOS-9.2 — Kafka consumer for `videos` topic → fan-out to follower feeds.
- [ ] KOS-9.3 — REST endpoint: paginated user feed.
- [ ] KOS-9.4 — Producer for `notifications` topic when new feed entries land.
- [ ] KOS-9.5 — Producer for `analytics` topic on feed reads.

---

# P4 — Engagement & Insight
End-to-end user-visible features land here.

## KOS-10 — Notification Service
- [ ] KOS-10.1 — Kafka consumer for `notifications` topic.
- [ ] KOS-10.2 — Pluggable channel adapters (log, email-stub, in-app).
- [ ] KOS-10.3 — Per-user notification preferences (Postgres or Redis).
- [ ] KOS-10.4 — Dead-letter topic + retry policy.

## KOS-11 — Analytics Service
- [ ] KOS-11.1 — Kafka consumer for `analytics` topic.
- [ ] KOS-11.2 — Daily/hourly rollups stored in `analytics_db`.
- [ ] KOS-11.3 — REST endpoints feeding the operator dashboard (counts, trends, top content).
- [ ] KOS-11.4 — Backfill script for historical events.

---

# P5 — Frontends
Two independently-deployable apps. Design: [`docs/tech/frontends.md`](docs/tech/frontends.md).

## KOS-19 — Split Frontends (React + Vue)
Supersedes the legacy KOS-12 epic. The completed `KOS-12.1` (npm scripts) is preserved for history below.

- [ ] KOS-19.1 — Rename `dashboard/` → `dashboard-react/`; update docs and references.
- [ ] KOS-19.2 — Add missing webpack loaders (`html-webpack-plugin`, `ts-loader`, `css-loader`, `style-loader`) so the React app builds and serves.
- [ ] KOS-19.3 — Implement React routes: `/` (feed), `/search`, `/videos/:id`, `/upload`, `/notifications`, `/profile`.
- [ ] KOS-19.4 — Wire React app to Keycloak via the JS adapter; gate routes by `user`/`creator` roles.
- [ ] KOS-19.5 — Scaffold `dashboard-vue/` with Vue 3 + TypeScript + Vuetify + Vite (or webpack 5).
- [ ] KOS-19.6 — Implement Vue routes: `/` (KPI overview), `/content`, `/users`, `/services`, `/kafka`.
- [ ] KOS-19.7 — Wire Vue app to Keycloak; gate to `moderator`/`admin` roles only.
- [ ] KOS-19.8 — Auto-generate TS API clients from each service's OpenAPI spec; share the generator config.
- [ ] KOS-19.9 — Per-frontend CI pipeline (lint, typecheck, build).
- [ ] KOS-19.10 — Per-frontend Dockerfile + nginx static-serving image.

## KOS-12 — Frontend Dashboard *(superseded by KOS-19)*
- [x] KOS-12.1 — Add npm `scripts` block (`start`, `build`, `lint`, `test`) to [`dashboard/package.json`](dashboard/package.json).

---

# P6 — Production readiness
The system is ready for actual users.

## KOS-13 — Observability
- [ ] KOS-13.1 — Spring Boot Actuator + Micrometer registry on every service.
- [ ] KOS-13.2 — Prometheus scrape config for all `/actuator/prometheus` endpoints.
- [ ] KOS-13.3 — Grafana provisioning: per-service dashboards + system overview.
- [ ] KOS-13.4 — Structured JSON logging (Logback encoder) with trace/span IDs.
- [ ] KOS-13.5 — OpenTelemetry agent for distributed traces (Jaeger or Tempo backend).
- [ ] KOS-13.6 — Loki for log aggregation (queryable from the same Grafana).
- [ ] KOS-13.7 — Dead-letter dashboards: per-DLT queue depth visible in Grafana.

*KOS-14 (Tests) was promoted to P0 — see above.*

## KOS-15 — Security Hardening
- [ ] KOS-15.1 — Externalised secrets via env vars + Kubernetes secrets.
- [ ] KOS-15.2 — OWASP Dependency-Check + Trivy image scan in CI.
- [ ] KOS-15.3 — Strict CORS / CSP at the gateway.
- [ ] KOS-15.4 — mTLS or service-mesh encryption between internal services (Istio/Linkerd).
- [ ] KOS-15.5 — Audit log for admin operations (sink to `analytics` topic).
- [x] KOS-15.6 — Dashboard dependency CVE clearance — 0 vulnerabilities on this branch.

## KOS-16 — Documentation & Developer Experience
- [ ] KOS-16.1 — `README.md` per module (< 100 lines, links to detail in `docs/`).
- [ ] KOS-16.2 — OpenAPI specs aggregated and rendered (Swagger UI) behind the gateway.
- [ ] KOS-16.3 — `docs/onboarding.md` — five-minute "clone and run" guide.
- [x] KOS-16.4 — Fix link inconsistency in tutorial.
- [x] KOS-16.5 — Add `docs/README.md` index.
- [ ] KOS-16.6 — Architecture Decision Records under `docs/adr/` for significant choices.
- [ ] KOS-16.7 — Runbooks under `docs/runbooks/` — one per alert.

---

# P7 — Deployment
Cluster-ready.

## KOS-17 — Kubernetes / Production Deployment
- [ ] KOS-17.1 — Helm chart per service with shared library chart for common values.
- [ ] KOS-17.2 — HorizontalPodAutoscaler on CPU + Kafka lag.
- [ ] KOS-17.3 — Ingress + cert-manager for TLS.
- [ ] KOS-17.4 — External Secrets Operator integration.
- [ ] KOS-17.5 — Blue/green or canary release strategy via Argo Rollouts.

---

# P8 — Agent / Developer tooling
Already largely done. Tracked here for completeness.

## KOS-18 — Agent-Agnostic Rules Layout
- [x] KOS-18.1 — Source-of-truth `agents/` directory split into `rules/` and `skills/`.
- [x] KOS-18.2 — `AGENTS.md` root entry point.
- [x] KOS-18.3 — `CLAUDE.md` wrapper.
- [x] KOS-18.4 — `.cursor/rules/main.mdc` wrapper for Cursor.
- [ ] KOS-18.5 — `.github/copilot-instructions.md` wrapper for GitHub Copilot.
- [ ] KOS-18.6 — Pre-commit hook that fails when a wrapper drifts from the canonical rules.

---

# P9 — Beyond baseline
Optional enhancements that materially raise quality, reliability, or developer velocity. Take when there is bandwidth, or promote into the band where they unlock the most value.

## Reliability & data integrity
- [ ] KOS-S1 — **Outbox pattern** for reliable Kafka publishing from services that also write to Postgres.
- [ ] KOS-S2 — **Kafka schema registry** (Avro or Protobuf) replacing JSON-by-convention.
- [ ] KOS-S3 — **Saga orchestration** for multi-service workflows (upload → process → notify).

## Performance
- [ ] KOS-S4 — **gRPC for internal RPC** — keep REST at the edge.
- [ ] KOS-S5 — **CQRS read models for analytics** with materialized views.
- [ ] KOS-S6 — **Multi-region read replicas** for the metadata DB.
- [ ] KOS-S8 — **Multi-tier cache** (Caffeine in-process + Redis shared).
- [ ] KOS-S9 — **HTTP/2 (or HTTP/3)** at the gateway.

## Observability
- [ ] KOS-S10 — **SLOs per service** with error-budget burn-rate alerts.
- [ ] KOS-S11 — **Continuous profiling** (Pyroscope).

## Security
- [ ] KOS-S12 — **Signed Kafka events** for audit trails on regulated data.
- [ ] KOS-S13 — **Image signing** (cosign) and admission-controller verification.
- [ ] KOS-S14 — **Zero-trust ingress** with WAF in front of the gateway.

## Operability
- [ ] KOS-S7 — **Feature flags** (Unleash, PostHog, or LaunchDarkly).
- [ ] KOS-S15 — **Chaos testing** (LitmusChaos or Chaos Mesh).
- [ ] KOS-S16 — **Backup & restore drills** for Postgres and Elasticsearch on a schedule.

## Frontend (beyond KOS-19)
- [ ] KOS-S17 — **Storybook** for the design system.
- [ ] KOS-S18 — **Visual regression tests** (Chromatic or Percy).
