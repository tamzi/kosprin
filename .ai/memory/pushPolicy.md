---
name: pushPolicy
description: Agents push only when the user explicitly asks; never proactively, never with --no-verify or -f.
type: memory
---

# Push policy

AI agents in this repo **never push proactively**. The user must explicitly say "push", "push it", "go ahead and push", or similar before any `git push` runs. The same rule applies to creating PRs (`gh pr create`), opening releases, and pushing tags. `--no-verify`, `--force`, and `-f` are never used. Force-push to `master` is never used and warned about if requested.

**Why:** the user retains control over when code leaves their laptop. Hooks are the only project-wide enforcement and bypassing them silently puts the repo into a state nothing else will catch. A previous incident — an agent that auto-pushed after each commit — produced a noisy origin and landed half-finished work.

**How to apply:** after any commit, **stop**. Wait for the user. When asked to push, run [`../workflows/prePushChecklist.md`](../workflows/prePushChecklist.md) first; if any gate fails, fix the cause and try again. Never `--no-verify`.
