---
name: securityReview
description: End-to-end security pass for any diff with a security surface; veto power on merge.
type: workflow
uses: [securityAuditor, runSecurityScan, auditSecrets, threatModelFeature, patchVulnerability]
---

# Workflow — Security review

End-to-end security pass for any change that touches a security-relevant surface. Runs as step 9 of [featureDelivery](featureDelivery.md), and standalone any time the user says "give this a security look".

## Trigger

Any of:
- The diff touches auth, an externally reachable endpoint, deserialisation, file I/O, child-process spawning, secrets, or dependencies.
- The diff adds or upgrades a Docker base image.
- The user explicitly asked for a security review.

## Owner

[securityAuditor](../agents/securityAuditor.md). Has veto authority — if the workflow ends with unresolved findings, the PR does not merge.

## Steps

| # | Step                                  | Skill                                                          | Done when                                                       |
|---|---------------------------------------|----------------------------------------------------------------|-----------------------------------------------------------------|
| 1 | Identify the surface                  | (manual) — diff walk + the trigger checklist above.            | List of qualifying surfaces produced.                           |
| 2 | Run scan layers                       | [runSecurityScan](../skills/runSecurityScan.md)                | gitleaks + dep-CVE + image (if applicable) all complete.        |
| 3 | Triage findings                       | [patchVulnerability](../skills/patchVulnerability.md)          | Each HIGH/CRITICAL is fixed, pinned-with-rationale, or ticketed. |
| 4 | Threat model                          | [threatModelFeature](../skills/threatModelFeature.md)          | One-page STRIDE table; ≥ Medium items have mitigations.         |
| 5 | Auth path review                      | (manual against [architecture rules](../rules/architecture.md))| Keycloak-validated, correct service-to-service flow.            |
| 6 | Egress + logging review               | (manual against [architecture rules](../rules/architecture.md))| Egress allowlist respected; no PII / secret logged.             |
| 7 | Record decisions                      | (manual)                                                       | Append a one-liner to [`../memory/decisions.md`](../memory/decisions.md) for any pinned vuln or accepted residual risk. |
| 8 | Verdict                               | —                                                              | Approve / request changes posted on the PR.                     |

## Gate to merge

- No HIGH or CRITICAL is unresolved (fixed, justified-pinned, or has a KOS-* ticket with a deadline).
- The threat model has no unaddressed Medium-or-higher item.
- The decisions ledger reflects any residual risk that was accepted.

## Composes with

- Embedded in [featureDelivery](featureDelivery.md) (step 9).
- Embedded weekly in [weeklyMaintenance](weeklyMaintenance.md).
- Drives: a follow-up [bumpDependency](../skills/bumpDependency.md) chain when CVEs land.
