# Improvement Ideas

A backlog of "beyond baseline" improvements. Each one is optional — they are not required for the system to function, but they materially raise quality, reliability, or developer velocity. Higher-leverage items should graduate to the [Jira board](../jiraboard.md) under `KOS-S*`.

## How to read this file
Status legend: `[ ]` to do · `[~]` in progress · `[x]` done.

Tick a box (`[ ]` → `[x]`) the moment the work merges to `master`. Use `[~]` for anything actively being worked on a branch. Items that started here and graduated to the Jira board should keep both ticked here and the linked `KOS-*` ticket updated.

## Build & developer experience
- [x] **Version catalog** (`gradle/libs.versions.toml`) — one source of truth for dependency versions; eliminates the brittle `ext[...]` map. *(KOS-1.2)*
- [ ] **Convention plugins** under `buildLogic/` — turn each service module's build into a 3-line file. *(KOS-1.3)*
- [ ] **Generated TypeScript clients** for the dashboard, produced from each service's OpenAPI spec at build time. Removes hand-rolled `axios` boilerplate and stops typing drift. *(KOS-12.5)*
- [~] **Pre-commit + commit-msg hooks** mirroring the parent project conventions (atomic commits, past-tense messages). *Commit-msg validator is active; pre-commit not yet wired.*
- [x] **Renovate or Dependabot** for dependency PRs. *Dependabot active on `master`.*

## Reliability & data integrity
- [ ] **Outbox pattern** for Kafka publishing from services that also write to Postgres. Removes the "did the DB write commit but the Kafka send fail?" gap. *(KOS-S1)*
- [ ] **Schema registry (Avro/Protobuf)** with Confluent or Apicurio. Replaces JSON-by-convention; CI gates incompatible changes. *(KOS-S2)*
- [ ] **Idempotency keys** at the gateway for create operations (e.g. `Idempotency-Key` header) so a client retry doesn't double-create.
- [ ] **Saga / orchestration** for multi-service workflows (upload → process → notify). Today there is no compensation path if a stage fails halfway. *(KOS-S3)*
- [ ] **Dead-letter dashboards** in Grafana — visible queue depth per DLT, not just metrics in Prometheus.

## Performance
- [ ] **Multi-tier cache** (caffeine in-process + Redis shared). Avoids hammering Redis for hottest keys.
- [ ] **CQRS read models for analytics** — denormalised tables / materialized views rebuilt from the `analytics` topic, separate from the OLTP path. *(KOS-S5)*
- [ ] **gRPC for internal RPC** — keep REST/JSON at the edge, move service-to-service calls to gRPC for lower latency and codegen contracts. *(KOS-S4)*
- [ ] **HTTP/2 (or HTTP/3) at the gateway** for client-facing endpoints.
- [ ] **Read replicas** for the metadata DB once read traffic dominates writes. *(KOS-S6)*

## Observability
- [ ] **OpenTelemetry traces** — Tempo backend + Grafana for traces alongside metrics. *(KOS-13.5)*
- [ ] **Loki for logs** — keeps logs queryable from the same Grafana instance as metrics/traces.
- [ ] **SLOs per service** with error-budget burn-rate alerts, replacing simple threshold alerts.
- [ ] **Continuous profiling** (Pyroscope) — find hot methods without a JFR session.

## Security
- [x] **Dependency CVE clearance** — dashboard deps audited and bumped; 0 vulnerabilities. *Landed on `improvements` branch.*
- [ ] **Service mesh (Istio or Linkerd)** for mTLS, retries, and policy without code changes. *(KOS-15.4)*
- [ ] **Per-tenant rate limits** at the gateway.
- [ ] **Signed Kafka events** for audit trails on regulated data.
- [ ] **Image signing** (cosign) and admission-controller verification.
- [ ] **Zero-trust ingress** with WAF in front of the gateway.

## Operability
- [ ] **Feature flags** (Unleash, PostHog, or LaunchDarkly) to gate risky rollouts. Ties cleanly into a canary deployment story. *(KOS-S7)*
- [ ] **Argo Rollouts or Flagger** for canary/blue-green release strategies. *(KOS-17.5)*
- [ ] **Chaos testing** (LitmusChaos or Chaos Mesh) — fault injection in a sandbox cluster.
- [ ] **Backup & restore drills** for Postgres and Elasticsearch on a schedule.

## Frontend architecture
- [~] **Microfrontends with Module Federation** — React shell hosting React and Vue remotes, each independently deployable. *(KOS-19)*
- [ ] **Auto-generated TS API clients** from each service's OpenAPI spec.
- [ ] **Storybook** for the design system.
- [ ] **Visual regression tests** (Chromatic or Percy).

## Documentation
- [x] **Docs index** at `docs/README.md` covering tech, product, agents.
- [ ] **Architecture Decision Records** under `docs/adr/` for significant choices (Keycloak over custom auth, Kafka over RabbitMQ, etc.).
- [ ] **Module READMEs** (< 100 lines each) per the project doc rules — link to the deeper docs in `docs/tech/`. *(KOS-16.1)*
- [ ] **Runbooks** under `docs/runbooks/` — one per alert.
- [ ] **Onboarding script** that exercises the docs end-to-end. *(KOS-16.3)*

## Agent / DX
- [x] **Agent-agnostic rules layout** — `agents/` source of truth with `AGENTS.md`, `CLAUDE.md`, and `.cursor/rules/` wrappers. *(KOS-18.1–18.4)*
- [ ] **Copilot instructions wrapper** at `.github/copilot-instructions.md`. *(KOS-18.5)*
- [ ] **Drift hook** that fails when a wrapper diverges from the canonical rules. *(KOS-18.6)*

## Quick wins (under a day each)
- [x] Fix the `loadbalancer.md` vs `LoadBalancer.md` link inconsistency in [`tutorial.md`](tutorial.md).
- [x] Add `scripts` to [`dashboard/package.json`](../dashboard/package.json).
- [x] Add a `docs/README.md` index.
- [x] Convert the root `ext[...]` block to a version catalog.
- [x] Add `apply false` to the root `build.gradle.kts` plugins.
