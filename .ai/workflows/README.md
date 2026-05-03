---
name: workflowsIndex
description: Index of multi-skill flows that chain agents and skills end-to-end.
type: workflow
---

# Workflows

Multi-skill flows. Each workflow chains skills (and the agents that own them) into a sequence that delivers a unit of value end-to-end. When a task spans more than one skill, look here first.

A workflow is the answer to "how do we get from intent to merged + green?" — it does not re-derive the steps, it stitches them.

Status legend: ✅ defined · 🟡 stubbed · ⬜ proposed

## Catalog

| Workflow                                              | Status | Trigger                                                        |
|-------------------------------------------------------|--------|----------------------------------------------------------------|
| [featureDelivery](featureDelivery.md)                 | ✅     | A new KOS-* ticket, plan → ship.                               |
| [prePushChecklist](prePushChecklist.md)               | ✅     | Before any `git push`.                                         |
| [securityReview](securityReview.md)                   | ✅     | Anything touching auth, external surface, or data egress.      |
| [weeklyMaintenance](weeklyMaintenance.md)             | ✅     | Scheduled — Monday morning, no live work pending.              |
| [releaseFlow](releaseFlow.md)                         | ✅     | Cutting a release, version bump → image promoted to staging.   |
| [newServiceBootstrap](newServiceBootstrap.md)         | 🟡     | Standing up a new `*-service/` module from scratch.            |
| [incidentResponse](incidentResponse.md)               | 🟡     | Something is on fire in staging or prod.                       |
| [boardConsistencyAudit](boardConsistencyAudit.md)     | 🟡     | Reconcile jiraboard state against code and docs.               |

## Anatomy

Each workflow file lays out:

1. **Trigger** — what kicks the flow off (event, schedule, ticket type).
2. **Owner** — which [agent role](../agents/README.md) drives the flow.
3. **Steps** — ordered, each step links to a skill or another workflow.
4. **Gates** — conditions that must hold to advance to the next step.
5. **Done condition** — what "this workflow is complete" looks like.

A workflow does not duplicate skill content — it points at the skill and adds glue (handoffs, gates, who picks up next).

## Composition diagram

```
intent ─► featureDelivery ─► prePushChecklist ─► openPullRequest ─► reviewer ─► merged
              │                      │                                  │
              ├─► writeAdr            ├─► checkDependencyUpdates         ├─► securityReview (if surface)
              ├─► addNewService       ├─► runTests                       └─► tagRelease (if release-cut)
              ├─► addRestEndpoint     ├─► runEvalSuite (if AI feature)
              └─► addObservability    └─► runSecurityScan
```

Workflows are intentionally cheap to author — start by listing the steps you keep doing in the same order, then promote that into a file.
