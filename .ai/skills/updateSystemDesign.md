---
name: updateSystemDesign
description: Refresh the system design document after architecture-relevant changes.
type: skill
---

# Skill - Update System Design

Use this when a change affects module boundaries, datastore ownership, messaging contracts, auth, or deployment shape.

## Steps

1. Read [`../../docs/tech/systemdesign.md`](../../docs/tech/systemdesign.md), [`../../docs/tech/services.md`](../../docs/tech/services.md), and [`../../docs/tech/messaging.md`](../../docs/tech/messaging.md).
2. Update only the affected diagram, table row, or rationale.
3. Link any related ADR or decision entry.
4. Run [jiraboardConsistencyCheck](jiraboardConsistencyCheck.md) if the design now implies new work.

## Done condition

- The docs match the code and board.
- Any hard-to-reverse choice has an ADR or decision entry.
