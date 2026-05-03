---
name: planTicket
description: Slice a feature request into KOS-* sub-tasks on jiraboard.md before any code is written.
type: skill
uses: [commitChanges]
---

# Skill — Plan a ticket

Slice a feature request into KOS-* sub-tasks on [`/jiraboard.md`](../../jiraboard.md) before any code is written.

## When to use
- The user describes work larger than ~3 files or ~1 hour of effort.
- The work touches more than one module.
- There is no existing KOS-* ticket that fits.

## Steps

1. **Find the home epic.** Open the jiraboard, scan the priority bands (P0–P9). Match the request to the band whose theme fits (reliability, performance, security, …). If no epic fits, propose one to the user before writing tickets.
2. **Draft the slice.** Each sub-task should be one shippable PR (1–3 files when possible, per the [commit conventions](../rules/commitConventions.md)). Order them by dependency — resources → models → utilities → state → UI → integration.
3. **Use the next free `KOS-X.Y` ID** under the epic. Do not reuse closed IDs.
4. **Phrase each ticket as a deliverable**, not a verb: `KOS-15.3 — Outbox table migration` (not `Add outbox table`). The verb belongs in the commit, not the ticket.
5. **Mark dependencies inline** with `(blocks: KOS-15.4)` or `(blocked-by: KOS-12.1)` so the order is explicit.
6. **Insert the slice into the jiraboard** under the right epic, in priority order. Keep formatting consistent with neighbouring entries.
7. **Confirm with the user** before starting work. Show the slice in the response so they can prune or re-order.

## Done condition
- Every sub-task has a unique KOS-* ID, a one-line deliverable phrase, and (if needed) explicit dependency markers.
- The jiraboard diff is one commit, message `Added KOS-X.* breakdown for <feature name>`.
- The user has approved the slice.

## Composes with
- [featureDelivery](../workflows/featureDelivery.md) — this is step 1.
- [productManager](../agents/productManager.md) — owns the priority call.
- [architect](../agents/architect.md) — consult when slicing crosses module boundaries.
