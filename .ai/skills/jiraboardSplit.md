---
name: jiraboardSplit
description: Split an oversized KOS-* ticket into atomic shippable pieces.
type: skill
---

# Skill — Split a jiraboard ticket

A ticket that does not fit one atomic commit (1–3 files) is too big. Split it before starting the work.

## When to use

- The work behind a ticket touches > 3 unrelated files.
- The ticket bundles several layers (schema + service + controller + tests for several entities).
- A previous attempt at the ticket grew into a large diff that the [reviewer](../agents/reviewer.md) bounced.

## Procedure

1. Read the ticket on [`/jiraboard.md`](../../jiraboard.md). Re-read its description.
2. Decompose by **dependency layer** — schema/data → entities → repositories → services → DTOs → controllers → tests → docs.
3. For each layer that has work, create a sub-ticket via [jiraboardAddTicket](jiraboardAddTicket.md). Use sub-IDs that follow naturally (e.g. `KOS-15` → `KOS-15.1`, `KOS-15.2`, …).
4. Mark the parent ticket as **superseded by** the new sub-ticket IDs. Either:
   - Convert it to an epic line if it had no checkbox, or
   - Strike its checkbox out (`- [ ] ~~KOS-15~~ — split into KOS-15.1..15.4`) and let the sub-tickets carry the work.
5. Confirm with the user before any code is written. The split is the new contract.
6. Commit message: `Split KOS-15 into 15.1..15.4` (past tense, no period).

## Hard rules

- Each sub-ticket must be independently shippable. If sub-ticket B requires sub-ticket A's code in flight (not yet merged), they're really one ticket — collapse them or order them strictly.
- Do not split solely to inflate ticket count. Splitting that produces "added empty stub" + "filled in stub" sub-tickets is busy-work — keep them together.
- Re-use of a freed-up ticket ID is forbidden. If KOS-15 was closed and you need to add 15.1 later, just add it; don't recycle 15.

## Composes with

- [jiraboardAddTicket](jiraboardAddTicket.md) — for each new sub-ticket.
- [planTicket](planTicket.md) — for the broader slice.
