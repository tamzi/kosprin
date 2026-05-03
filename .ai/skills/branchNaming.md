---
name: branchNaming
description: Pick a branch name aligned with the active jiraboard priority band and the ticket(s) it carries.
type: skill
---

# Skill — Branch naming

Branch names tell a reviewer at a glance which priority band and which tickets are in flight.

## When to use

- Before starting work on a new ticket or set of tickets.
- Never commit on `master` — branch first.

## Procedure

1. Identify the dominant ticket scope from [`/jiraboard.md`](../../jiraboard.md):
   - **One sub-task** → `<band>/KOS-X.Y-<short-slug>`
   - **A whole epic** → `<band>/KOS-X-<short-slug>`
   - **A whole priority band** → `<band>` (e.g. the current `improvements` branch carries P5–P7 work)
2. `<band>` is one of: `p0`, `p1`, …, `p9`, `chore`, `hotfix`, `release`.
3. Slugs are kebab-case, ≤ 4 words, lowercase only.
4. Branch from a fresh `master` — `git fetch origin && git rebase origin/master` first.
5. Never reuse a branch name. After merge, delete the branch (`git push origin --delete <name>` only with explicit user approval).

## Examples

- `p3/KOS-14.3-integration-test-scaffold`
- `p4/KOS-15-outbox-pattern`
- `chore/dep-bump-spring-boot-3-5`
- `hotfix/KOS-22.1-kafka-dlt-poison-loop`
- `release/v0.1.0`

## Don't

- Don't put dates or initials in branch names — `git log` knows both.
- Don't branch from a stale base; pull `master` first.
- Don't reuse `improvements` or any historical "kitchen-sink" branch for new tickets — start a focused branch.

## References

- [`/jiraboard.md`](../../jiraboard.md) — priority bands and ticket IDs.
- [`../agents/releaseManager.md`](../agents/releaseManager.md) — owns `release/*` branches.
