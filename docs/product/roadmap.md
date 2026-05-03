# Roadmap

The board lives in [`/jiraboard.md`](../../jiraboard.md). This file groups epics into phases.

## Phase 0 — Foundation (must land first)
Goal: a developer can clone the repo, run a local stack, and have any service start without errors.

- KOS-1 Build System Foundation
- KOS-2 Common Module
- KOS-3 Local Development Infrastructure

Exit criteria: `./gradlew build` succeeds, `docker compose up` brings up Postgres + Redis + Kafka + ES + Keycloak, every service boots and reports healthy.

## Phase 1 — Edge & Identity
Goal: clients can authenticate and reach a service through the gateway.

- KOS-4 API Gateway
- KOS-5 Identity & Access (Keycloak)

Exit criteria: a JWT-authenticated request from the dashboard reaches a stub endpoint on the metadata service.

## Phase 2 — Core write path
Goal: content can be created and asynchronously processed.

- KOS-6 Metadata Service
- KOS-7 Video Processing Service

Exit criteria: posting a new video record produces a `videos` event, the processor consumes it, and a `video-processed` event updates the metadata row.

## Phase 3 — Read paths
Goal: clients can search and consume feeds.

- KOS-8 Search Service
- KOS-9 Feed Service

Exit criteria: search returns indexed videos within a few seconds of creation; followers see new uploads in their feed.

## Phase 4 — Engagement & insight
- KOS-10 Notification Service
- KOS-11 Analytics Service
- KOS-12 Frontend Dashboard

Exit criteria: dashboard renders real charts driven by analytics-service rollups; notifications fire on new feed entries.

## Phase 5 — Production readiness
- KOS-13 Observability
- KOS-14 Testing & CI
- KOS-15 Security Hardening
- KOS-16 Documentation & Developer Experience

Exit criteria: green CI, ≥70% line coverage, dashboards in Grafana, no secrets in repo.

## Phase 6 — Deploy
- KOS-17 Kubernetes / Production Deployment

Exit criteria: helm-installable; runs on a kind cluster locally and on a managed cluster (GKE/EKS/AKS) in staging.
