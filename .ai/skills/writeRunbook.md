---
name: writeRunbook
description: Capture an operational runbook for an alert or recurring failure mode.
type: skill
---

# Skill - Write Runbook

Use this when a failure mode needs repeatable operator steps.

## Steps

1. Name the symptom, likely causes, first checks, mitigation, and escalation trigger.
2. Link dashboards, logs, service docs, and source paths instead of copying code.
3. Add the runbook to `docs/README.md`.

## Done condition

- A new operator can diagnose the issue without reading the original incident thread.
