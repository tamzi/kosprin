---
name: addObservability
description: Add logs, metrics, traces, and health signals for a new code path.
type: skill
---

# Skill - Add Observability

Use this when a new endpoint, consumer, job, or integration path needs operational visibility.

## Steps

1. Add structured logs with trace and correlation identifiers.
2. Add metrics for throughput, latency, failures, and queue depth where relevant.
3. Ensure health or readiness reflects new required dependencies.
4. Update [`../../docs/tech/observability.md`](../../docs/tech/observability.md) when a new signal becomes part of operations.

## Done condition

- An operator can tell whether the path is healthy without reading application code.
