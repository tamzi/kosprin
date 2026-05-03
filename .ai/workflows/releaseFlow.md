---
name: releaseFlow
description: Cut → scan → eval → tag → promote → smoke → soak → rollback path verified.
type: workflow
uses: [releaseManager, securityAuditor, evalSteward, tagRelease, runSecurityScan, runEvalSuite, rollbackDeployment]
---

# Workflow — Release flow

The cut between "merged on `master`" and "running in production". Owned end-to-end by [releaseManager](../agents/releaseManager.md).

## Trigger

- Enough merged work warrants a cut, OR
- A scheduled release window, OR
- A hotfix to address a production issue.

## Owner

[releaseManager](../agents/releaseManager.md). [securityAuditor](../agents/securityAuditor.md) gates the image-scan step. [evalSteward](../agents/evalSteward.md) gates the eval step if AI features are in the cut.

## Steps

| # | Step                          | Skill / Tool                                                    | Gate                                                            |
|---|-------------------------------|-----------------------------------------------------------------|-----------------------------------------------------------------|
| 1 | Confirm freeze                | (manual)                                                        | No required-for-this-release PR is in flight.                   |
| 2 | Pick version                  | (manual SemVer)                                                 | Version chosen, agreed with reviewer if ambiguous.              |
| 3 | Compose changelog             | `git log <last-tag>..HEAD --format='- %s'`                      | Sectioned (Added/Changed/Fixed/Removed/Security).               |
| 4 | Update changelog file         | (one commit, `Updated CHANGELOG for vX.Y.Z`)                    | One `.md`, one commit.                                          |
| 5 | Build images                  | CI pipeline                                                     | All service images built from the release SHA.                  |
| 6 | Scan images                   | [runSecurityScan](../skills/runSecurityScan.md) (image layer)   | No HIGH / CRITICAL unresolved.                                  |
| 7 | Run evals (if AI in cut)      | [runEvalSuite](../skills/runEvalSuite.md)                       | No regression > 2% on tracked metrics, or documented trade.     |
| 8 | Tag                           | [tagRelease](../skills/tagRelease.md)                           | Annotated tag pushed (with explicit user approval).             |
| 9 | Promote to staging            | (per project promotion process)                                 | Smoke tests pass; `/actuator/health` green on every service.    |
| 10| Verify rollback path          | (manual rehearsal in staging)                                   | Previous tag's image still in registry; rollback script works.  |
| 11| Promote to prod               | (per project promotion process — requires explicit approval)    | Soak window started.                                            |
| 12| Watch dashboards              | (oncall)                                                        | No SLO burn over the soak window.                               |

## Done condition

- The new tag is in the registry and running in prod.
- The changelog entry is on `master`.
- A one-line release summary in [`../memory/decisions.md`](../memory/decisions.md): `2026-05-04 — released vX.Y.Z; notable: <one-liner>`.

## Rollback

If anything in steps 9–12 trips, [rollbackDeployment](../skills/rollbackDeployment.md) is the next move. Rollback is not a failure — pushing a broken release without a verified rollback path is.

## Composes with

- Pulls in: [tagRelease](../skills/tagRelease.md), [runSecurityScan](../skills/runSecurityScan.md), [runEvalSuite](../skills/runEvalSuite.md), [rollbackDeployment](../skills/rollbackDeployment.md).
- Picks up after [featureDelivery](featureDelivery.md) once enough merged work warrants a cut.
