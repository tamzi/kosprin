---
name: boardConsistencyAudit
description: Audit jiraboard checkboxes against code, docs, and shipped state.
type: workflow
uses: [productManager, jiraboardConsistencyCheck, jiraboardAddTicket]
---

# Workflow - Board Consistency Audit

## Trigger

Weekly maintenance, before release, or whenever the board appears stale.

## Owner

[productManager](../agents/productManager.md).

## Steps

1. Run [jiraboardConsistencyCheck](../skills/jiraboardConsistencyCheck.md).
2. Tick completed work only when code and docs support it.
3. Add missing work through [jiraboardAddTicket](../skills/jiraboardAddTicket.md).
4. Leave blocked items marked with a one-line reason.

## Done condition

- [`../../jiraboard.md`](../../jiraboard.md) reflects current reality.
