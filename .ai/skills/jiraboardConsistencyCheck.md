---
name: jiraboardConsistencyCheck
description: Reconcile ticked KOS-* tickets against code reality; surface drift.
type: skill
---

# Skill — Jiraboard consistency check

Make sure [`/jiraboard.md`](../../jiraboard.md) tells the truth. A board that says "done" when the code says "missing" is worse than no board.

## When to use

- Weekly, as part of [weeklyMaintenance](../workflows/weeklyMaintenance.md).
- Before any release cut — see [releaseFlow](../workflows/releaseFlow.md).
- Whenever the user asks "what's actually shipped on this branch?".

## Procedure

1. **Sample 10–20 ticked tickets at random.** For each:
   - Find the closing commit: `git log --all --grep "KOS-X.Y" --oneline`.
   - If no commit references the ticket, find the commit by looking at code: where does the deliverable live?
   - Confirm the deliverable exists at the path you'd expect.
2. **Sample 10 unticked tickets.** For each:
   - Confirm the deliverable does *not* exist yet.
   - If it does exist, that's drift — tick the ticket in a follow-up commit and note it in [`../memory/aiLayoutAdoption.md`](../memory/aiLayoutAdoption.md) or a new memory entry.
3. **Walk the priority bands** top to bottom. Each band's epic count and ticked-count should match the band's overall stated state.
4. **Verify the active band** mentioned in any wrapper docs (AGENTS.md, CLAUDE.md, the jiraboard's own preamble) matches the band that is actually in flight.

## Findings

Produce a short report:
- ✅ matched — count.
- ⚠️ ticked-but-missing — list with KOS IDs and what was expected.
- ⚠️ shipped-but-unticked — list with KOS IDs and the commit SHAs that delivered them.
- ➕ work that's clearly happening but has no ticket — propose new tickets via [jiraboardAddTicket](jiraboardAddTicket.md).

## Don't

- Don't auto-tick on the basis of inference. Surface the finding to the user; let them tick.
- Don't rewrite ticket descriptions during the audit. That's a separate doc commit.
- Don't grep history of branches that have been deleted — start from `master` and the active branches.

## Composes with

- [boardConsistencyAudit](../workflows/boardConsistencyAudit.md) — the broader workflow.
- [jiraboardAddTicket](jiraboardAddTicket.md) — for newly-discovered work.
