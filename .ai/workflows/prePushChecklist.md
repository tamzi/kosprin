---
name: prePushChecklist
description: Twelve gates every push must clear — build, tests, lint, deps, secrets, eval, user approval.
type: workflow
uses: [dependencyWarden, securityAuditor, checkDependencyUpdates, runTests, runEvalSuite, runSecurityScan, commitChanges]
---

# Workflow — Pre-push checklist

Every push runs this. No exceptions, no shortcuts. The pre-push git hook codifies most of it; this workflow is the human/agent-readable version so the gates stay visible.

## Trigger
About to run `git push` (with explicit user approval — see [`../rules/agentBehaviour.md`](../rules/agentBehaviour.md)).

## Owner
Whoever is about to push, with [dependencyWarden](../agents/dependencyWarden.md) on dep gates and [securityAuditor](../agents/securityAuditor.md) on security gates.

## Steps

| # | Gate                            | How to verify                                                              | Failure action                            |
|---|---------------------------------|----------------------------------------------------------------------------|--------------------------------------------|
| 1 | Branch is clean                 | `git status` reports nothing unstaged.                                     | Stash or commit first.                     |
| 2 | Commits are atomic + well-named | `git log master..HEAD --oneline` — every subject past-tense, < 72 chars.   | Reword via [commitChanges](../skills/commitChanges.md) (NEW commits, not amend). |
| 3 | One `.md` per commit            | `git log master..HEAD --stat` — no commit changes more than one `.md`.     | Split the commit.                          |
| 4 | Build passes                    | `./gradlew build`                                                          | Fix; do NOT push red.                      |
| 5 | Unit tests pass                 | `./gradlew test`                                                           | Fix; do NOT push red.                      |
| 6 | Integration tests pass          | `./gradlew :module:integrationTest` for touched modules.                   | Fix; do NOT push red.                      |
| 7 | Type-check (dashboard)          | `cd dashboard && npx tsc --noEmit`                                         | Fix; do NOT push red.                      |
| 8 | Linters pass                    | `./gradlew detekt` and `cd dashboard && npm run lint`                      | Fix.                                       |
| 9 | Dependency check                | [checkDependencyUpdates](../skills/checkDependencyUpdates.md)              | Bump cleanly-bumpable; ticket the rest.    |
| 10| Secret scan                     | `gitleaks git --redact -v --no-banner`                                     | Stop. Run [auditSecrets](../skills/auditSecrets.md). |
| 11| AI-backed change touched?       | If yes, [runEvalSuite](../skills/runEvalSuite.md) — paste diff into PR.    | Investigate any > 2% regression.           |
| 12| User has explicitly approved    | They typed "push" or equivalent in this conversation.                      | Stop. Ask.                                 |

## Done condition

All twelve gates pass. The push command runs without `--no-verify` or `--force`. The pre-push hook also passes.

## Composes with
- Drives: [openPullRequest](../skills/openPullRequest.md) — what comes immediately after.
- Driven by: [featureDelivery](featureDelivery.md) at step 7.
