# Kosprin Jira Board

Faux project key: **KOS**. Status legend: `[ ]` To Do · `[~]` In Progress · `[x]` Done · `[!]` Blocked.

Epics are ordered roughly by dependency. Items with no owner are unclaimed.

---

## KOS-1 — Build System Foundation
Get Gradle building cleanly across the multi-module workspace before any service code lands.

- [x] KOS-1.1 — Fix root [`build.gradle.kts`](build.gradle.kts): plugins should be declared with `apply false` and re-applied per submodule, not on the root project.
- [x] KOS-1.2 — Replace `ext[...]` version pinning with a `gradle/libs.versions.toml` version catalog.
- [ ] KOS-1.3 — Add `buildLogic/` convention plugins: `kosprin.kotlin-library`, `kosprin.spring-service`, `kosprin.kafka-consumer`.
- [x] KOS-1.4 — Populate every empty submodule `build.gradle.kts` (currently 0 bytes for all 8 backend modules).
- [ ] KOS-1.5 — Wire Detekt + ktlint + JaCoCo at the root with shared config.
- [x] KOS-1.6 — Add `dependencyResolutionManagement` block to `settings.gradle.kts` so the catalog applies to all modules.

## KOS-2 — Common Module
Shared types and cross-cutting concerns reused by every service.

- [ ] KOS-2.1 — Define error envelope types (`ApiError`, `ProblemDetail` in line with RFC 7807).
- [ ] KOS-2.2 — Define shared event base types and Kafka headers (correlation ID, source, schema version).
- [ ] KOS-2.3 — `Result<T>` / `Outcome<T>` helper for service-layer returns.
- [ ] KOS-2.4 — Global exception handler + `@ControllerAdvice` autoconfig.
- [ ] KOS-2.5 — Tracing/MDC filter for correlation-ID propagation across HTTP and Kafka.

## KOS-3 — Local Development Infrastructure
A one-command local stack so devs can `docker compose up` and run any service against real dependencies.

- [ ] KOS-3.1 — `docker-compose.yml` with PostgreSQL, Redis, Kafka (KRaft mode), Elasticsearch, Keycloak.
- [ ] KOS-3.2 — Per-service `Dockerfile` (multi-stage, JLink runtime image).
- [ ] KOS-3.3 — Seeded Keycloak realm export and Postgres init scripts checked in under `infra/`.
- [ ] KOS-3.4 — Kafka topic auto-create / init script (videos, video-processed, notifications, analytics).
- [ ] KOS-3.5 — Add `Makefile` or `taskfile.yml` for common dev tasks (`up`, `down`, `logs`, `psql`, `kafka-console`).

## KOS-4 — API Gateway
Spring Cloud Gateway as the single ingress for all services.

- [ ] KOS-4.1 — Route table for all 6 services (path predicates + load-balanced URIs).
- [ ] KOS-4.2 — Keycloak OIDC integration; JWT validation filter.
- [ ] KOS-4.3 — Per-route rate limiter backed by Redis.
- [ ] KOS-4.4 — Global CORS, request logging, and correlation-ID injection filters.
- [ ] KOS-4.5 — `/actuator/health` aggregation across downstream services.
- [ ] KOS-4.6 — Circuit breaker + retry policies (Resilience4j).

## KOS-5 — Identity & Access (Keycloak)
- [ ] KOS-5.1 — Realm export `kosprin-realm.json` with `kosprin-gateway`, `kosprin-services` clients.
- [ ] KOS-5.2 — Roles: `user`, `creator`, `moderator`, `admin`.
- [ ] KOS-5.3 — Resource-server config in each service to validate JWT and map roles to authorities.
- [ ] KOS-5.4 — Optional: client-credentials flow for service-to-service calls.

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

## KOS-10 — Notification Service
- [ ] KOS-10.1 — Kafka consumer for `notifications` topic.
- [ ] KOS-10.2 — Pluggable channel adapters (log, email-stub, in-app).
- [ ] KOS-10.3 — Per-user notification preferences (Postgres or Redis).
- [ ] KOS-10.4 — Dead-letter topic + retry policy.

## KOS-11 — Analytics Service
- [ ] KOS-11.1 — Kafka consumer for `analytics` topic.
- [ ] KOS-11.2 — Daily/hourly rollups stored in `analytics_db`.
- [ ] KOS-11.3 — REST endpoints feeding the dashboard (counts, trends, top content).
- [ ] KOS-11.4 — Backfill script for historical events.

## KOS-12 — Frontend Dashboard
- [x] KOS-12.1 — Add npm `scripts` block (`start`, `build`, `lint`, `test`) to [`dashboard/package.json`](dashboard/package.json).
- [ ] KOS-12.2 — Axios client + Zustand store wired to analytics-service endpoints.
- [ ] KOS-12.3 — Real charts via `chart.js` (replace placeholder text on the Home view).
- [ ] KOS-12.4 — Keycloak JS adapter for OIDC login.
- [ ] KOS-12.5 — Auto-generate TS API clients from each service's OpenAPI spec.

## KOS-13 — Observability
- [ ] KOS-13.1 — Spring Boot Actuator + Micrometer registry on every service.
- [ ] KOS-13.2 — Prometheus scrape config for all `/actuator/prometheus` endpoints.
- [ ] KOS-13.3 — Grafana provisioning: per-service dashboards + system overview.
- [ ] KOS-13.4 — Structured JSON logging (Logback encoder) with trace/span IDs.
- [ ] KOS-13.5 — OpenTelemetry agent for distributed traces (Jaeger or Tempo backend).

## KOS-14 — Testing & CI
- [ ] KOS-14.1 — Unit-test scaffolding per module (JUnit 5, MockK, Kotest).
- [ ] KOS-14.2 — Integration tests with Testcontainers (Postgres, Kafka, Redis, ES).
- [ ] KOS-14.3 — Contract tests between producers/consumers (Spring Cloud Contract or Pact).
- [ ] KOS-14.4 — GitHub Actions: `build`, `test`, `detekt`, image build & push.
- [ ] KOS-14.5 — Coverage gate (JaCoCo, ≥70% line, ≥60% branch).

## KOS-15 — Security Hardening
- [ ] KOS-15.1 — Externalised secrets via env vars + Kubernetes secrets (no plaintext in repo).
- [ ] KOS-15.2 — OWASP Dependency-Check + Trivy image scan in CI.
- [ ] KOS-15.3 — Strict CORS / CSP at the gateway.
- [ ] KOS-15.4 — mTLS or service-mesh encryption between internal services.
- [ ] KOS-15.5 — Audit log for admin operations (sink to `analytics` topic).

## KOS-16 — Documentation & Developer Experience
- [ ] KOS-16.1 — `README.md` per module (< 100 lines, links to detail in `docs/`).
- [ ] KOS-16.2 — OpenAPI specs aggregated and rendered (Swagger UI) behind the gateway.
- [ ] KOS-16.3 — `docs/onboarding.md` — five-minute "clone and run" guide.
- [x] KOS-16.4 — Fix link inconsistency: `tutorial.md` references `loadbalancer.md` but file is `LoadBalancer.md`.
- [x] KOS-16.5 — Add `docs/README.md` index to navigate the docs tree.

## KOS-17 — Kubernetes / Production Deployment
- [ ] KOS-17.1 — Helm chart per service with shared library chart for common values.
- [ ] KOS-17.2 — HorizontalPodAutoscaler on CPU + Kafka lag.
- [ ] KOS-17.3 — Ingress + cert-manager for TLS.
- [ ] KOS-17.4 — External Secrets Operator integration.
- [ ] KOS-17.5 — Blue/green or canary release strategy via Argo Rollouts.

## KOS-19 — Split Frontends (React + Vue)
Two independently-deployable single-page apps, each owning a different audience. See [`docs/tech/frontends.md`](docs/tech/frontends.md).

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

## KOS-18 — Agent-Agnostic Rules Layout
Rule and skill files vendor-neutral, with thin wrappers per assistant.

- [x] KOS-18.1 — Source-of-truth `agents/` directory split into `rules/` (constraints) and `skills/` (task playbooks).
- [x] KOS-18.2 — `AGENTS.md` root entry point.
- [x] KOS-18.3 — `CLAUDE.md` wrapper pointing at `AGENTS.md` and `agents/`.
- [x] KOS-18.4 — `.cursor/rules/main.mdc` wrapper for Cursor.
- [ ] KOS-18.5 — `.github/copilot-instructions.md` wrapper for GitHub Copilot.
- [ ] KOS-18.6 — Pre-commit hook that fails when a wrapper drifts from the canonical rules.

---

## Stretch / Improvement Backlog
See [docs/improvements.md](docs/improvements.md) for the full list. Highest-leverage items:

- [ ] KOS-S1 — Outbox pattern for reliable Kafka publishing from Metadata service.
- [ ] KOS-S2 — Kafka schema registry (Avro or Protobuf) instead of JSON-by-convention.
- [ ] KOS-S3 — Saga orchestration for multi-service workflows (upload → process → notify).
- [ ] KOS-S4 — gRPC for internal service-to-service calls; keep REST at the edge.
- [ ] KOS-S5 — CQRS read models for analytics with materialized views.
- [ ] KOS-S6 — Multi-region read replicas for the metadata DB.
- [ ] KOS-S7 — Feature flags (Unleash or PostHog) gating risky rollouts.
