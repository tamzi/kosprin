---
name: estimateWork
description: Size a ticket using similar completed work and current module complexity.
type: skill
---

# Skill - Estimate Work

Use this before committing to a slice when effort or review size is unclear.

## Steps

1. Find similar completed KOS-* tickets in [`../../jiraboard.md`](../../jiraboard.md) and recent commit history.
2. Count touched modules, I/O boundaries, tests, docs, and migration risk.
3. Classify as small, medium, or large with the main uncertainty.
4. If large, split through [jiraboardSplit](jiraboardSplit.md).

## Done condition

- The estimate names the risk driver, not just the size.
- Oversized work has a proposed split.
