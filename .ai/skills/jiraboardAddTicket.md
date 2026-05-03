---
name: jiraboardAddTicket
description: Add a new KOS-* ticket under the right priority band and epic.
type: skill
---

# Skill — Add a jiraboard ticket

Add a new `KOS-*` entry without disturbing the band ordering or the existing IDs.

## When to use

- A new sub-task is required by the active work that did not exist before.
- A user asks to capture a follow-up the agent uncovered.
- An epic needs to grow a new sub-story.

## Procedure

1. Read [`/jiraboard.md`](../../jiraboard.md). Find the **priority band** the work fits (P0–P9 for normal work, "Beyond baseline" for wishlist).
2. Find the **epic** within that band whose theme matches. If none fits, propose creating a new epic to the user before adding a ticket.
3. Pick the **next free `KOS-X.Y`** under the chosen epic. Do not reuse closed IDs. Sub-task IDs are sequential per epic.
4. Compose the ticket as a **deliverable**, not a verb:
   - `- [ ] KOS-X.Y — <noun phrase that names the deliverable>`
   - Reference dependencies inline if relevant: `(blocks: KOS-X.Z)`, `(blocked-by: KOS-X.W)`.
5. Insert the line in priority order under the epic — easiest first if order matters, otherwise dependency order.
6. If the new ticket changes the epic's done-condition, update the epic's description in the same commit.
7. Commit message: `Added KOS-X.Y to <epic-name>` (past tense, no period).

## Don't

- Don't reorder existing IDs. The board's history depends on them.
- Don't add tickets to closed epics. Reopen the epic explicitly with the user first.
- Don't add tickets that say "decide whether…" — that's a planning conversation, not a deliverable.

## Composes with

- [planTicket](planTicket.md) — when slicing a feature into many tickets at once.
- [jiraboardSplit](jiraboardSplit.md) — when an existing ticket is too large.
- [jiraboardConsistencyCheck](jiraboardConsistencyCheck.md) — periodic audit.
