---
name: featureDelivery
description: End-to-end ticket → merged PR — plan, design, implement, verify, document, ship.
type: workflow
uses: [productManager, architect, implementer, testEngineer, reviewer, securityAuditor, planTicket, writeAdr, runTests, runEvalSuite, openPullRequest, reviewPullRequest, prePushChecklist]
---

# Workflow — Feature delivery

End-to-end from "user described a feature" to "merged on `master`". This is the canonical flow that most other workflows feed into.

## Trigger
A new feature request, a freshly opened KOS-* epic, or a sub-task that hasn't started.

## Owner
Begins with [productManager](../agents/productManager.md). Hands off across roles as it advances.

## Steps

| # | Step                          | Owner               | Skill                                                       | Gate                                                                        |
|---|-------------------------------|---------------------|-------------------------------------------------------------|-----------------------------------------------------------------------------|
| 1 | Plan                          | productManager      | [planTicket](../skills/planTicket.md)                       | User approved the slice.                                                    |
| 2 | Design (if non-trivial)       | architect           | [writeAdr](../skills/writeAdr.md)                           | ADR `Accepted` or decision deemed local enough to skip.                     |
| 3 | Implement                     | implementer         | [addNewService](../skills/addNewService.md) / [addRestEndpoint](../skills/addRestEndpoint.md) / [addKafkaTopic](../skills/addKafkaTopic.md) / etc. | Tests added, code follows [codeStyle](../rules/codeStyle.md). |
| 4 | Verify                        | implementer         | [runTests](../skills/runTests.md), [runEvalSuite](../skills/runEvalSuite.md) (if AI-backed) | All green. UI exercised in browser if applicable.                |
| 5 | Document                      | docKeeper           | [updateModuleReadme](../skills/updateModuleReadme.md)       | README still ≤ 100 lines, no code in `.md`.                                 |
| 6 | Commit                        | implementer         | [commitChanges](../skills/commitChanges.md)                 | Atomic, past-tense, hooks pass.                                             |
| 7 | Pre-push                      | dependencyWarden    | [prePushChecklist](prePushChecklist.md)                     | Every gate in that workflow passed.                                         |
| 8 | Open PR                       | implementer         | [openPullRequest](../skills/openPullRequest.md)             | User approved the push. PR body filled in.                                  |
| 9 | Security review (if surface)  | securityAuditor     | [securityReview](securityReview.md)                         | All findings triaged.                                                       |
| 10| Code review                   | reviewer            | [reviewPullRequest](../skills/reviewPullRequest.md)         | Explicit approve / request changes.                                         |
| 11| Merge                         | implementer         | —                                                           | CI green; reviewer approved; security cleared if applicable.                |
| 12| Mark ticket done              | productManager      | —                                                           | Strike the `[ ]` to `[x]` in [`/jiraboard.md`](../../jiraboard.md).         |

## Done condition
- The KOS-* ticket is `[x]`.
- The PR is merged.
- Any ADR / changelog / memory entry that the work generated is also merged.

## Variants
- **Hotfix** — skip steps 1–2, condense 5; keep 7–10. Always document the post-fix follow-up if you cut corners.
- **AI-backed feature** — step 4 must include [runEvalSuite](../skills/runEvalSuite.md) and step 8 must paste the eval diff into the PR body.
- **New service** — step 3 starts with [addNewService](../skills/addNewService.md), then chains into the [newServiceBootstrap](newServiceBootstrap.md) workflow before continuing.

## Composes with
- [prePushChecklist](prePushChecklist.md) — embedded at step 7.
- [securityReview](securityReview.md) — embedded at step 9 when applicable.
- [releaseFlow](releaseFlow.md) — picks up after step 11 once enough merged work warrants a cut.
