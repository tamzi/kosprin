---
name: incidentResponse
description: First-response workflow for staging or production incidents.
type: workflow
uses: [securityAuditor, releaseManager, tailServiceLogs, inspectKafkaTopic, rollbackDeployment]
---

# Workflow - Incident Response

## Trigger

An environment is unhealthy, users are impacted, or a deployment fails verification.

## Owner

The current operator owns triage. [securityAuditor](../agents/securityAuditor.md) joins if the incident touches security. [releaseManager](../agents/releaseManager.md) owns rollback.

## Steps

1. State impact, affected service, and start time.
2. Gather evidence through [tailServiceLogs](../skills/tailServiceLogs.md) and [inspectKafkaTopic](../skills/inspectKafkaTopic.md) where relevant.
3. Mitigate first, then preserve enough evidence for root cause.
4. Roll back through [rollbackDeployment](../skills/rollbackDeployment.md) if the current release is the cause.
5. File follow-up KOS-* work after service is stable.

## Done condition

- Impact is mitigated and follow-up work is tracked.
