# Docs Index

The repo's documentation, organised by audience.

## Product
- [`product/product.md`](product/product.md) — what kosprin is and who it's for.
- [`product/roadmap.md`](product/roadmap.md) — phased plan that maps to the [Jira board](../jiraboard.md).

## Tech
- [`tech/systemdesign.md`](tech/systemdesign.md) — architecture diagram and component responsibilities.
- [`tech/services.md`](tech/services.md) — per-module summary, ports, topics, datastores.
- [`tech/messaging.md`](tech/messaging.md) — Kafka topics, headers, idempotency conventions.
- [`tech/security.md`](tech/security.md) — Keycloak/IAM, service-to-service auth, secrets.
- [`tech/observability.md`](tech/observability.md) — metrics, logs, traces, alerts.
- [`tech/buildSystem.md`](tech/buildSystem.md) — Gradle multi-module setup and convention plugins.
- [`tech/localDevelopment.md`](tech/localDevelopment.md) — clone-to-run instructions.
- [`tech/LoadBalancer.md`](tech/LoadBalancer.md) — why there is no load-balancer microservice.

## Tutorial
- [`tutorial.md`](tutorial.md) — narrative walk-through of the architecture.

## Improvements
- [`improvements.md`](improvements.md) — backlog of optional enhancements beyond the baseline.

## Project tracking
- [`/jiraboard.md`](../jiraboard.md) — epics and sub-tasks (faux Jira project key `KOS`).

## Agents
- [`/AGENTS.md`](../AGENTS.md) — entry point for any AI assistant working in the repo.
- [`/agents/`](../agents/) — rules and skill playbooks (the source of truth referenced by `CLAUDE.md` and `.cursor/rules/`).
