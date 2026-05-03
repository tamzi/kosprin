---
name: commitChanges
description: Stage and compose an atomic past-tense commit; one .md per commit; hooks-clean; no --amend.
type: skill
uses: [jiraboardTick]
---

# Skill — Commit changes

Atomic commits, past-tense subject, no body. The full rule lives in [`../rules/commitConventions.md`](../rules/commitConventions.md); this is the operational recipe.

## Steps

1. **Check what is staged and unstaged**: `git status` and `git diff` (and `git diff --staged`). Run all three in parallel.
2. **Check recent commit style**: `git log --oneline -10`. Match the local convention (past tense, capitalised first word, no period, no `feat:` prefix).
3. **Group changes by atomic unit.** A single commit changes 1–3 files unless the files are tightly coupled (a class and its test). Split by dependency layer: resources → models → utilities → state → UI → integration.
4. **One `.md` per commit.** Never stage two doc files together. If a code change requires a doc update, ship them in separate commits.
5. **Stage explicitly** by filename: `git add path/to/file`. Never `git add -A` or `git add .` — they sweep up `.env`, secret files, and stray binaries.
6. **Write the subject** in past tense, < 72 chars, capitalised, no trailing period. Verb conveys intent (`Added`, `Fixed`, `Removed`, `Renamed`, `Refactored`, `Tightened`, `Documented`).
7. **No body** unless the user explicitly asked. The verb + noun should carry the full intent.
8. **Commit**: `git commit -m "<subject>"`.
9. **If the commit hook fails**, fix the underlying issue and create a NEW commit. Never `--amend` (would rewrite the previous commit, which the user may not have approved). Never `--no-verify`.
10. **Verify** with `git log -1 --stat` that exactly the intended files are in.

## Bad subject lines (and why)

| Bad                                    | Why                                  |
|----------------------------------------|--------------------------------------|
| `feat: add timezone utility`           | Conventional-commits prefix.         |
| `fix memory leak`                      | Lowercase, wrong tense.              |
| `Fixed memory leak.`                   | Trailing period.                     |
| `Updated stuff`                        | Vague — name the noun.               |
| `WIP - working on KOS-15.3`            | WIP commits should not be on shared branches. |

## Done condition

- `git log -1 --stat` shows the expected files and a clean past-tense subject.
- The pre-commit and commit-msg hooks passed.
- No `.env`, key, or unrelated file was staged by accident.

## Composes with
- [openPullRequest](openPullRequest.md) — what comes after.
- [prePushChecklist](../workflows/prePushChecklist.md) — gates a push, not a commit, but the commit shape determines whether the push will be ergonomic.
