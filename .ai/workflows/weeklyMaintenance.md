---
name: weeklyMaintenance
description: Monday sweep — dep bumps, security scans, eval drift, doc rot, board hygiene.
type: workflow
uses: [dependencyWarden, securityAuditor, evalSteward, checkDependencyUpdates, bumpDependency, runSecurityScan, runEvalSuite]
---

# Workflow — Weekly maintenance

The Monday-morning sweep that keeps the repo from rotting between feature work. Cheap to run, expensive to skip.

## Trigger

Weekly schedule (Monday morning). Or any time it has been more than a week since the last run.

## Owner

Rotates between [dependencyWarden](../agents/dependencyWarden.md) (deps), [securityAuditor](../agents/securityAuditor.md) (scans), and [evalSteward](../agents/evalSteward.md) (evals). For a single-person project, the same agent walks all three.

## Steps

| # | Step                                  | Skill                                                       | Output                                              |
|---|---------------------------------------|-------------------------------------------------------------|-----------------------------------------------------|
| 1 | Dep update sweep                      | [checkDependencyUpdates](../skills/checkDependencyUpdates.md) | List of bumps + tickets for the major ones.       |
| 2 | Bump cleanly-bumpable                 | [bumpDependency](../skills/bumpDependency.md) (per dep)     | One PR per dep, or one batched PR if all minor.     |
| 3 | Audit `# pinned:` comments            | (manual scan of `gradle/libs.versions.toml` + `package.json`) | Stale pins removed; live ones still justified.    |
| 4 | Security scan                         | [runSecurityScan](../skills/runSecurityScan.md)             | All three layers clean or triaged.                  |
| 5 | Eval drift check (AI features)        | [runEvalSuite](../skills/runEvalSuite.md)                   | No silent regression > 2% on tracked metrics.       |
| 6 | Doc rot check                         | (manual) — `grep -nR 'TODO\|FIXME\|XXX' docs/` and skim each module README. | Stale TODOs ticketed or removed.    |
| 7 | Memory hygiene                        | (manual) — read [`../memory/decisions.md`](../memory/decisions.md). | Any superseded entry marked, links fixed.        |
| 8 | Jiraboard hygiene                     | (manual) — close stale `[ ]` items that have already shipped, re-prioritise next sprint. | Board is current.                |

## Done condition

- All eight steps ran. Anything that wasn't fixed has a KOS-* ticket with a deadline.
- A one-line summary entry in [`../memory/decisions.md`](../memory/decisions.md): `2026-05-04 — weekly maintenance: bumped X, Y; deferred Z (KOS-N)`.

## Composes with

- Pulls in: [checkDependencyUpdates](../skills/checkDependencyUpdates.md), [bumpDependency](../skills/bumpDependency.md), [runSecurityScan](../skills/runSecurityScan.md), [runEvalSuite](../skills/runEvalSuite.md).
- Drives downstream [featureDelivery](featureDelivery.md) work for any newly-filed ticket.
