---
name: rollbackDeployment
description: Roll a failed deployment back to the previous known-good release.
type: skill
---

# Skill - Rollback Deployment

Use this when staging or production verification fails after a release promotion.

## Steps

1. Identify the affected service, current version, and previous known-good version.
2. Confirm the rollback command with the user for production.
3. Restore the previous image or chart version.
4. Verify health, smoke tests, logs, and dashboards.
5. Record the rollback reason in [`../memory/decisions.md`](../memory/decisions.md).

## Done condition

- Users are back on the previous known-good version and follow-up work is ticketed.
