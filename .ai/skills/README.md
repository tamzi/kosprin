---
name: skillsIndex
description: Index of single-task playbooks organised by lifecycle phase (Plan → Design → Implement → Verify → Document → Ship → Operate).
type: skill
---

# Skills

Single-task playbooks. Each file is the answer to "how do I do X here?" with enough specificity that an assistant new to the repo can finish the task without re-deriving steps.

A skill is one task, ordered steps, the gotchas that bite the first-time runner, and a clear "done" condition. Multi-task chains live under [`../workflows/`](../workflows/).

Status legend: ✅ implemented · 🟡 stubbed (frontmatter + outline only) · ⬜ proposed (not yet written)

## Lifecycle phases (May 2026 baseline)

### Plan
| Skill                                                 | Status | Purpose                                                     |
|-------------------------------------------------------|--------|-------------------------------------------------------------|
| [planTicket](planTicket.md)                           | ✅     | Slice a feature request into KOS-* sub-tasks on the jiraboard. |
| [writePrd](writePrd.md)                               | 🟡     | Draft a product requirements doc for an epic.               |
| [estimateWork](estimateWork.md)                       | 🟡     | Size a ticket using prior actuals from `git log`.           |

### Design
| Skill                                                 | Status | Purpose                                                     |
|-------------------------------------------------------|--------|-------------------------------------------------------------|
| [writeAdr](writeAdr.md)                               | ✅     | Capture an architecture decision under `docs/adr/`.         |
| [updateSystemDesign](updateSystemDesign.md)           | 🟡     | Refresh the diagram in `docs/tech/systemdesign.md`.         |

### Implement
| Skill                                                 | Status | Purpose                                                     |
|-------------------------------------------------------|--------|-------------------------------------------------------------|
| [runLocalStack](runLocalStack.md)                     | ✅     | Bring up Postgres + Redis + Kafka + ES + Keycloak.          |
| [addNewService](addNewService.md)                     | ✅     | Cookie-cutter steps for a new `*-service` module.           |
| [addKafkaTopic](addKafkaTopic.md)                     | ✅     | Register a topic and wire producer/consumer correctly.      |
| [addRestEndpoint](addRestEndpoint.md)                 | 🟡     | Add a REST endpoint with DTO, validation, OpenAPI, tests.   |
| [addDbMigration](addDbMigration.md)                   | 🟡     | Forward-only Flyway migration with smoke test.              |
| [addObservability](addObservability.md)               | 🟡     | Wire metrics, traces, structured logs for a new code path.  |

### Verify
| Skill                                                 | Status | Purpose                                                     |
|-------------------------------------------------------|--------|-------------------------------------------------------------|
| [runTests](runTests.md)                               | ✅     | Unit + integration tests, single module or full tree.       |
| [runEvalSuite](runEvalSuite.md)                       | ✅     | Eval harness for AI-backed features (recall/precision/cost).|
| [runSecurityScan](runSecurityScan.md)                 | ✅     | Trivy + gitleaks + npm/Gradle audit for the current branch. |
| [auditSecrets](auditSecrets.md)                       | 🟡     | Deep `gitleaks` pass over history, redaction plan if hits.  |
| [threatModelFeature](threatModelFeature.md)           | 🟡     | Lightweight STRIDE pass before merging an external surface. |

### Document
| Skill                                                 | Status | Purpose                                                     |
|-------------------------------------------------------|--------|-------------------------------------------------------------|
| [updateModuleReadme](updateModuleReadme.md)           | 🟡     | Keep a module README under 100 lines, link to `docs/`.      |
| [writeRunbook](writeRunbook.md)                       | 🟡     | Capture an oncall runbook under `docs/runbooks/`.           |
| [updateAgentMemory](updateAgentMemory.md)             | 🟡     | Promote a recurring fact into [`../memory/`](../memory/).   |

### Ship
| Skill                                                 | Status | Purpose                                                     |
|-------------------------------------------------------|--------|-------------------------------------------------------------|
| [commitChanges](commitChanges.md)                     | ✅     | Atomic past-tense commit, one-doc-per-commit, verify hooks. |
| [openPullRequest](openPullRequest.md)                 | ✅     | Branch → push → `gh pr create` with the standard body.      |
| [reviewPullRequest](reviewPullRequest.md)             | ✅     | Standard review checklist (architecture / style / risk).    |
| [bumpDependency](bumpDependency.md)                   | ✅     | Bump a single dep cleanly with verification.                |
| [checkDependencyUpdates](checkDependencyUpdates.md)   | ✅     | Pre-push: list outdated deps and bump cleanly-bumpable.     |
| [tagRelease](tagRelease.md)                           | 🟡     | SemVer tag, changelog entry, container image push.          |

### Operate
| Skill                                                 | Status | Purpose                                                     |
|-------------------------------------------------------|--------|-------------------------------------------------------------|
| [tailServiceLogs](tailServiceLogs.md)                 | 🟡     | `docker compose logs -f` with JSON pretty-printing.         |
| [inspectKafkaTopic](inspectKafkaTopic.md)             | 🟡     | `kafka-console-consumer` with the right group + DLT toggle. |
| [rollbackDeployment](rollbackDeployment.md)           | 🟡     | Roll a service back to the prior image tag.                 |
| [patchVulnerability](patchVulnerability.md)           | 🟡     | Triage a CVE, pin or upgrade, ship the fix.                 |

## How to use

1. **Picking a skill** — search by task name. If nothing fits, use the closest skill as a template and ship the new skill alongside the work.
2. **Reading a skill** — the steps are ordered. Don't skip "before reporting done" sections; that's where the silent failures live.
3. **Authoring a skill** — keep it tight: prerequisites, ordered steps, "done" condition, common failures. No copied source-code snippets; link to the source file or include the actual command. See [planTicket.md](planTicket.md) as the canonical shape.
4. **Composing skills** — when work spans more than one skill, that is a [workflow](../workflows/README.md), not a skill. Don't fork skills to inline another skill's steps.
