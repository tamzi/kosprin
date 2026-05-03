# Kosprin Jira Board

Faux project key: **KOS**. Status legend: `[ ]` to do · `[~]` in progress · `[x]` done · `[!]` blocked.

This board is the **single source of truth** for project work. Work the priority bands top-down — P0 lands before P1, and so on. Items inside a band can run in parallel.

The old `docs/improvements.md` backlog was consolidated into this file; the standalone improvements doc has been removed.

## Working agreement
- **Finish all stories in an epic before starting a different epic.** Don't cherry-pick.
- **Within an epic, complete the current story end-to-end before opening another.**
- If a story is genuinely **blocked** by a dependency, mark it `[!]` with a one-line reason in the bullet, then continue with the rest of the same epic. Never silently skip a story to jump elsewhere.
- **Tests and Dockerfiles ride with the service.** Each service epic ends with `*.N — Unit tests`, `*.N — Integration tests`, `*.N — Dockerfile`. There is no separate "Tests" or "Dockerize all services" epic.
- **Stretch items in P8 do not start until** there is real bandwidth, or until one of them unlocks higher-priority work — at which point promote it into the band where it belongs.

This rule is also captured in [`agents/rules/agentBehaviour.md`](agents/rules/agentBehaviour.md) so every AI assistant in the repo follows it.

---

# P0 — Foundation
Unblocks every other band. Until P0 is done, services cannot start cleanly and there is no local stack to run them against.

## KOS-1 — Build System Foundation
- [x] KOS-1.1 — Fix root [`build.gradle.kts`](build.gradle.kts): plugins declared with `apply false`, applied per submodule.
- [x] KOS-1.2 — Replace `ext[...]` version pinning with a `gradle/libs.versions.toml` version catalog.
- [x] KOS-1.3 — `buildLogic/` convention plugins: `kosprin.kotlin-library`, `kosprin.spring-library`, `kosprin.spring-service`, `kosprin.kafka-consumer`.
- [x] KOS-1.4 — Populate every empty submodule `build.gradle.kts`.
- [x] KOS-1.5 — Detekt + ktlint + JaCoCo + JUnit 6 BOM + MockK + Kotest assertions + Testcontainers + JaCoCo coverage gate (≥70% line, ≥60% branch) wired through convention plugins; integration test source set; ben-manes versions plugin at root for `dependencyUpdates`.
- [x] KOS-1.6 — Add `dependencyResolutionManagement` block to `settings.gradle.kts`.

## KOS-2 — Common Module
- [x] KOS-2.1 — Define error envelope types (`ApiError`, `ProblemDetail` per RFC 7807).
- [x] KOS-2.2 — Define shared event base types and Kafka headers (correlation ID, source, schema version).
- [x] KOS-2.3 — `Outcome<T>` helper for service-layer returns.
- [x] KOS-2.4 — Global exception handler + `@RestControllerAdvice` autoconfig.
- [x] KOS-2.5 — Tracing/MDC filter for correlation-ID propagation across HTTP and Kafka.

## KOS-3 — Local Development Infrastructure
- [x] KOS-3.1 — `docker-compose.yml` with PostgreSQL, Redis, Kafka (KRaft mode), Elasticsearch, Keycloak.
- [x] KOS-3.2 — Postgres init scripts checked in under `infra/postgres-init/`.
- [x] KOS-3.3 — Kafka topic auto-create via `kafka-init` one-shot container in compose.
- [x] KOS-3.4 — `Makefile` for common dev tasks (`up`, `down`, `reset`, `logs`, `psql`, `redis-cli`, `kafka-topics`, `build`, `test`).

---

# P1 — First runnable slice
A user can hit a service through the gateway with a JWT.

## KOS-4 — Identity & Access (Keycloak)
- [ ] KOS-4.1 — Realm export `kosprin-realm.json` with `kosprin-gateway` and `kosprin-services` clients.
- [ ] KOS-4.2 — Roles: `user`, `creator`, `moderator`, `admin`.
- [ ] KOS-4.3 — Resource-server starter library in `common` for JWT validation and role-to-authority mapping (consumed by every service).
- [ ] KOS-4.4 — Client-credentials flow for service-to-service calls.
- [ ] KOS-4.5 — Integration tests for the resource-server library against a real Keycloak container.

## KOS-5 — API Gateway
- [ ] KOS-5.1 — Route table for all 6 services (path predicates + load-balanced URIs).
- [ ] KOS-5.2 — Keycloak OIDC integration; JWT validation filter (consumes KOS-4.3).
- [ ] KOS-5.3 — Per-route rate limiter backed by Redis (incl. per-tenant variant).
- [ ] KOS-5.4 — Global CORS, request logging, and correlation-ID injection filters.
- [ ] KOS-5.5 — `/actuator/health` aggregation across downstream services.
- [ ] KOS-5.6 — Circuit breaker + retry policies (Resilience4j).
- [ ] KOS-5.7 — `Idempotency-Key` header support for create operations.
- [ ] KOS-5.8 — Unit tests.
- [ ] KOS-5.9 — Integration tests with Testcontainers (Redis for rate limiter, Keycloak for JWT).
- [ ] KOS-5.10 — Dockerfile (multi-stage, JRE runtime image).

## KOS-6 — Metadata Service
- [ ] KOS-6.1 — Domain model: `Video`, `User`, `Tag`. JPA entities + Flyway migrations.
- [ ] KOS-6.2 — REST endpoints: create/read/update/list metadata.
- [ ] KOS-6.3 — Redis read-through cache for hot paths (`GET /videos/{id}`, list-by-user).
- [ ] KOS-6.4 — Kafka producer for `videos` topic on create.
- [ ] KOS-6.5 — OpenAPI spec published at `/v3/api-docs`.
- [ ] KOS-6.6 — Unit tests.
- [ ] KOS-6.7 — Integration tests with Testcontainers (Postgres + Kafka + Redis).
- [ ] KOS-6.8 — Contract test (producer side, `videos` topic).
- [ ] KOS-6.9 — Dockerfile (multi-stage, JRE runtime image).

---

# P2 — Write path completion
The system can ingest content and process it asynchronously end-to-end.

## KOS-7 — Video Processing Service
- [ ] KOS-7.1 — Kafka consumer for `videos` topic.
- [ ] KOS-7.2 — Simulated processing pipeline (sleep + log; pluggable handler interface for future ffmpeg).
- [ ] KOS-7.3 — Object-storage abstraction (local FS by default, S3-compatible adapter later).
- [ ] KOS-7.4 — Kafka producer for `video-processed` topic with thumbnail URL + duration.
- [ ] KOS-7.5 — Idempotent consumer (dedupe by `eventId`).
- [ ] KOS-7.6 — Wire metadata-service to consume `video-processed` and update video status. Lives in this epic so the consumer ships after the producer (KOS-7.4) exists, not before.
- [ ] KOS-7.7 — Unit tests.
- [ ] KOS-7.8 — Integration tests with Testcontainers (Kafka).
- [ ] KOS-7.9 — Contract verifier for `videos`; producer-side test for `video-processed`; consumer-side test for the metadata wire-up (KOS-7.6).
- [ ] KOS-7.10 — Dockerfile.

---

# P3 — Read paths
Clients can search and consume feeds.

## KOS-8 — Search Service
- [ ] KOS-8.1 — Elasticsearch index template for `videos` (analyzer + mapping).
- [ ] KOS-8.2 — Kafka consumer for `videos` topic → index documents.
- [ ] KOS-8.3 — Search REST endpoint with pagination, faceting on tags.
- [ ] KOS-8.4 — Redis cache for popular query strings (TTL ~60s).
- [ ] KOS-8.5 — Reindex CLI / admin endpoint.
- [ ] KOS-8.6 — Unit tests.
- [ ] KOS-8.7 — Integration tests with Testcontainers (Elasticsearch + Kafka + Redis).
- [ ] KOS-8.8 — Contract verifier for `videos`.
- [ ] KOS-8.9 — Dockerfile.

## KOS-9 — Feed Service
- [ ] KOS-9.1 — JPA model for `FeedItem`, `Follow` graph + Flyway migrations.
- [ ] KOS-9.2 — Kafka consumer for `videos` topic → fan-out to follower feeds.
- [ ] KOS-9.3 — REST endpoint: paginated user feed.
- [ ] KOS-9.4 — Producer for `notifications` topic when new feed entries land.
- [ ] KOS-9.5 — Producer for `analytics` topic on feed reads.
- [ ] KOS-9.6 — Unit tests.
- [ ] KOS-9.7 — Integration tests with Testcontainers (Postgres + Kafka).
- [ ] KOS-9.8 — Contract verifier for `videos`; producer-side tests for `notifications` and `analytics`.
- [ ] KOS-9.9 — Dockerfile.

---

# P4 — Engagement & Insight
End-to-end user-visible features land here.

## KOS-10 — Notification Service
- [ ] KOS-10.1 — Kafka consumer for `notifications` topic.
- [ ] KOS-10.2 — Pluggable channel adapters (log, email-stub, in-app).
- [ ] KOS-10.3 — Per-user notification preferences (Postgres or Redis).
- [ ] KOS-10.4 — Dead-letter topic + retry policy.
- [ ] KOS-10.5 — Unit tests.
- [ ] KOS-10.6 — Integration tests with Testcontainers (Kafka + Postgres).
- [ ] KOS-10.7 — Contract verifier for `notifications`.
- [ ] KOS-10.8 — Dockerfile.

## KOS-11 — Analytics Service
- [ ] KOS-11.1 — Kafka consumer for `analytics` topic.
- [ ] KOS-11.2 — Daily/hourly rollups stored in `analytics_db`.
- [ ] KOS-11.3 — REST endpoints feeding the operator dashboard (counts, trends, top content).
- [ ] KOS-11.4 — Backfill script for historical events.
- [ ] KOS-11.5 — Unit tests.
- [ ] KOS-11.6 — Integration tests with Testcontainers (Kafka + Postgres).
- [ ] KOS-11.7 — Contract verifier for `analytics`.
- [ ] KOS-11.8 — Dockerfile.

---

# P5 — Frontends
Two independently-deployable apps. Design: [`docs/tech/frontends.md`](docs/tech/frontends.md).

## KOS-12 — dashboard-react (end-user)
- [x] KOS-12.1 — Add npm `scripts` block (`start`, `build`, `lint`, `test`) to `dashboard/package.json`.
- [ ] KOS-12.2 — Rename `dashboard/` → `dashboard-react/`; update docs and references.
- [ ] KOS-12.3 — Add missing webpack loaders (`html-webpack-plugin`, `ts-loader`, `css-loader`, `style-loader`) so the React app builds and serves.
- [ ] KOS-12.4 — Implement React routes: `/` (feed), `/search`, `/videos/:id`, `/upload`, `/notifications`, `/profile`.
- [ ] KOS-12.5 — Wire React app to Keycloak via the JS adapter; gate routes by `user`/`creator` roles.
- [ ] KOS-12.6 — Auto-generate TS API client from each service's OpenAPI spec.
- [ ] KOS-12.7 — Per-app CI pipeline (lint, typecheck, build).
- [ ] KOS-12.8 — Dockerfile + nginx static-serving image.

## KOS-13 — dashboard-vue (operator/analytics)
- [ ] KOS-13.1 — Scaffold `dashboard-vue/` with Vue 3 + TypeScript + Vuetify + Vite.
- [ ] KOS-13.2 — Implement Vue routes: `/` (KPI overview), `/content`, `/users`, `/services`, `/kafka`.
- [ ] KOS-13.3 — Wire Vue app to Keycloak; gate to `moderator`/`admin` roles only.
- [ ] KOS-13.4 — Auto-generate TS API client (share generator config with KOS-12).
- [ ] KOS-13.5 — Per-app CI pipeline (lint, typecheck, build).
- [ ] KOS-13.6 — Dockerfile + nginx static-serving image.

---

# P6 — Production readiness
The system is ready for actual users.

## KOS-14 — Observability
- [ ] KOS-14.1 — Spring Boot Actuator + Micrometer registry on every service.
- [ ] KOS-14.2 — Prometheus scrape config for all `/actuator/prometheus` endpoints.
- [ ] KOS-14.3 — Grafana provisioning: per-service dashboards + system overview.
- [ ] KOS-14.4 — Structured JSON logging (Logback encoder) with trace/span IDs.
- [ ] KOS-14.5 — OpenTelemetry agent for distributed traces (Jaeger or Tempo backend).
- [ ] KOS-14.6 — Loki for log aggregation (queryable from the same Grafana).
- [ ] KOS-14.7 — Dead-letter dashboards: per-DLT queue depth visible in Grafana.

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

## KOS-17 — CI/CD Pipeline
- [x] KOS-17.1 — GitHub Actions workflow: `build`, `test`, `detekt`, `ktlint`, `dependencyUpdates`, JaCoCo coverage gate; test + coverage report artifacts uploaded.
- [ ] KOS-17.2 — Image build & push per service (uses each service's Dockerfile from its own epic).
- [ ] KOS-17.3 — Coverage upload to Codecov (or Coveralls).
- [ ] KOS-17.4 — Gradle build cache action.
- [ ] KOS-17.5 — Required-checks setup and PR labelling.

## KOS-18 — End-to-end Tests
- [ ] KOS-18.1 — E2E test harness running against the full Docker Compose stack.
- [ ] KOS-18.2 — Happy path: upload → process → search → feed → notify.
- [ ] KOS-18.3 — Failure path: invalid input rejected at the gateway.
- [ ] KOS-18.4 — Failure path: DLT routing on consumer error.
- [ ] KOS-18.5 — CI job that runs the E2E suite on a schedule (and pre-release).

## KOS-19 — Kubernetes / Production Deployment
- [ ] KOS-19.1 — Helm chart per service with shared library chart for common values.
- [ ] KOS-19.2 — HorizontalPodAutoscaler on CPU + Kafka lag.
- [ ] KOS-19.3 — Ingress + cert-manager for TLS.
- [ ] KOS-19.4 — External Secrets Operator integration.
- [ ] KOS-19.5 — Blue/green or canary release strategy via Argo Rollouts.

---

# P7 — Agent / Developer tooling
Already largely done. Tracked here for completeness.

## KOS-20 — Agent-Agnostic Rules Layout
- [x] KOS-20.1 — Source-of-truth `agents/` directory split into `rules/` and `skills/`.
- [x] KOS-20.2 — `AGENTS.md` root entry point.
- [x] KOS-20.3 — `CLAUDE.md` wrapper.
- [x] KOS-20.4 — `.cursor/rules/main.mdc` wrapper for Cursor.
- [ ] KOS-20.5 — `.github/copilot-instructions.md` wrapper for GitHub Copilot.
- [ ] KOS-20.6 — Pre-commit hook that fails when a wrapper drifts from the canonical rules.

---

# P8 — Beyond baseline
Optional enhancements that materially raise quality, reliability, or developer velocity. Take when there is bandwidth, or promote into the band where they unlock the most value. Each item is epic-sized — when promoted, it grows sub-stories like any other epic.

## Reliability & data integrity
- [ ] KOS-21 — **Outbox pattern** for reliable Kafka publishing from services that also write to Postgres.
- [ ] KOS-22 — **Kafka schema registry** (Avro or Protobuf) replacing JSON-by-convention.
- [ ] KOS-23 — **Saga orchestration** for multi-service workflows (upload → process → notify).

## Performance
- [ ] KOS-24 — **gRPC for internal RPC** — keep REST at the edge.
- [ ] KOS-25 — **CQRS read models for analytics** with materialized views.
- [ ] KOS-26 — **Multi-region read replicas** for the metadata DB.
- [ ] KOS-27 — **Multi-tier cache** (Caffeine in-process + Redis shared).
- [ ] KOS-28 — **HTTP/2 (or HTTP/3)** at the gateway.

## Observability
- [ ] KOS-29 — **SLOs per service** with error-budget burn-rate alerts.
- [ ] KOS-30 — **Continuous profiling** (Pyroscope).

## Security
- [ ] KOS-31 — **Signed Kafka events** for audit trails on regulated data.
- [ ] KOS-32 — **Image signing** (cosign) and admission-controller verification.
- [ ] KOS-33 — **Zero-trust ingress** with WAF in front of the gateway.

## Operability
- [ ] KOS-34 — **Feature flags** (Unleash, PostHog, or LaunchDarkly).
- [ ] KOS-35 — **Chaos testing** (LitmusChaos or Chaos Mesh).
- [ ] KOS-36 — **Backup & restore drills** for Postgres and Elasticsearch on a schedule.

## Frontend (beyond KOS-12 / KOS-13)
- [ ] KOS-37 — **Storybook** for the design system.
- [ ] KOS-38 — **Visual regression tests** (Chromatic or Percy).
