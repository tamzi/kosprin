---
name: jiraboardTick
description: Tick a KOS-* ticket on jiraboard.md in the same commit that closes it.
type: skill
---

# Skill — Jiraboard tick

Keep [`/jiraboard.md`](../../jiraboard.md) truthful. Every commit that closes a ticket flips its checkbox from `[ ]` to `[x]` in the **same commit** that ships the code.

## When to use

- Every commit that closes a `KOS-X.Y` ticket.
- After completing the last sub-task of an epic, the epic line gets ticked too.
- After completing every epic in a priority band, the band header may flip its summary line if one exists.

## Procedure

1. Open `jiraboard.md` and locate the ticket line — usually:
   `- [ ] KOS-X.Y — Description`
2. Flip the checkbox: `- [x]`. Leave the rest of the line untouched.
3. If the ticket was the **last** unticked sub-task in its epic, also tick the epic header's checkbox if it has one.
4. Stage `jiraboard.md` alongside the source files for the same commit. The pre-commit hook expects this pairing.

## Don't

- Don't tick a ticket without the code change that closes it. Tick = "this is done".
- Don't tick more than one ticket in one commit (atomic-commit rule).
- Don't reword the ticket line. The id and description are stable; if the description is wrong, that's a separate doc-only commit.
- Don't insert status emoji or timestamps. The checkbox is the only signal.

## Common failures

- **Ticked ticket reverted** — if the closing commit is reverted, the tick must be reverted in the same revert. Otherwise the board lies.
- **Two tickets in one commit** — split the commit. Use `git reset --soft HEAD^` then re-stage and re-commit (with the user's consent if it's already shared).

## References

- [`/jiraboard.md`](../../jiraboard.md) — the live board.
- [`commitChanges.md`](commitChanges.md) — composing the commit itself.
