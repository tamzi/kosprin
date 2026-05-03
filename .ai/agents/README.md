---
name: agentsIndex
description: Index of specialised role definitions for delegation and persona-adoption.
type: agent
---

# Agents

Specialised role definitions. Each file describes what the role owns, what it cares about, what it refuses to ship, and which skills it draws on. Use them two ways:

1. **Adopt the role** — when the work matches a role's remit, follow that file's checklist before acting (e.g. before approving a PR, run the `reviewer` checklist).
2. **Delegate to it** — when spawning a subagent for a focused task, paste the role file as the agent's persona so its priorities are concrete.

Agents are not separate processes — they're personas that any underlying assistant (Claude Code, Cursor, Codex, …) can take on.

Status legend: ✅ defined · 🟡 stubbed · ⬜ proposed

## Roster

| Agent                                              | Status | Owns                                                                                  |
|----------------------------------------------------|--------|---------------------------------------------------------------------------------------|
| [productManager](productManager.md)                | ✅     | PRDs, prioritisation against [`/jiraboard.md`](../../jiraboard.md), user-impact framing. |
| [architect](architect.md)                          | ✅     | System design, ADRs, module-boundary calls, sync-vs-async judgement.                  |
| [implementer](implementer.md)                      | 🟡     | Writing code that satisfies a ticket end-to-end (test + impl + docs).                 |
| [testEngineer](testEngineer.md)                    | 🟡     | Test strategy, integration coverage, regression confidence.                           |
| [reviewer](reviewer.md)                            | ✅     | PR review against architecture / style / commit / security rules.                     |
| [securityAuditor](securityAuditor.md)              | ✅     | Threat modelling, scan triage, secret hygiene, dependency CVEs.                       |
| [dependencyWarden](dependencyWarden.md)            | ✅     | Pre-push dep check, weekly bump cycle, pinned-package justifications.                 |
| [docKeeper](docKeeper.md)                          | 🟡     | README length, link-rot, `docs/README.md` index, docs hygiene.                        |
| [releaseManager](releaseManager.md)                | ✅     | Versioning, changelog, tag, image promotion, rollback playbook.                       |
| [evalSteward](evalSteward.md)                      | 🟡     | Eval suite for AI-backed features — owns the harness, gates regressions.              |

## When to use which

| Situation                                                                 | Agent             |
|---------------------------------------------------------------------------|-------------------|
| Backlog grooming, "what should we ship next?"                             | `productManager`  |
| New module / cross-service contract / new sync vs async question          | `architect`       |
| Ticket is clear, just needs to be built                                   | `implementer`     |
| Test strategy or integration coverage is unclear                          | `testEngineer`    |
| PR is open, decision = approve / request changes                          | `reviewer`        |
| New external surface, new auth path, dependency CVE, suspect secret leak  | `securityAuditor` |
| Pre-push dep check, weekly maintenance bump cycle                         | `dependencyWarden`|
| Module README drifting, link rot, doc tree restructure                    | `docKeeper`       |
| Cutting a release, rolling one back                                       | `releaseManager`  |
| AI-backed feature change (RAG, summariser, classifier, …)                 | `evalSteward`     |

## Authoring an agent

Every agent file has the same shape:

1. **Mandate** — one paragraph: what this agent owns, what it does not.
2. **Reads first** — the rules and skills this agent always consults.
3. **Checklist** — concrete bullets the agent must walk through before acting.
4. **Refuses to ship** — explicit veto conditions. The agent blocks the task until they're met.
5. **Composes with** — workflows that drive this agent.

Use [`reviewer.md`](reviewer.md) as the canonical example.
