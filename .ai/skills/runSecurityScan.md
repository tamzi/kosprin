---
name: runSecurityScan
description: Three-layer security pass — gitleaks secrets + Gradle/npm dep CVEs + trivy image scan.
type: skill
---

# Skill — Run security scan

Trivy + gitleaks + Gradle/npm audit pass. Run before any PR that touches: auth, external surfaces, deserialisation, file I/O, child processes, secrets, or dependencies. Run weekly regardless via [weeklyMaintenance](../workflows/weeklyMaintenance.md).

## What "scan" means here

Three layers, in order from cheapest to most expensive:

| Layer            | Tool                  | Catches                                                 |
|------------------|-----------------------|---------------------------------------------------------|
| Source secrets   | `gitleaks`            | API keys, JWTs, passwords accidentally committed.       |
| Dependency CVEs  | `./gradlew dependencyCheckAnalyze`, `npm audit` | Known vulnerable transitive deps.    |
| Image / runtime  | `trivy image <tag>`   | Vulnerable OS packages, misconfig in container layers.  |

## Steps

1. **Secrets pass** (seconds):
   ```
   gitleaks git --redact -v --no-banner
   ```
   - Any HIGH or CRITICAL → stop and run [auditSecrets](auditSecrets.md). Do not push.
   - LOW (entropy false positives) → triage: add to `.gitleaksignore` with a one-line reason, or rotate.

2. **Dependency CVE pass** (1–3 min):
   ```
   ./gradlew dependencyCheckAnalyze        # JVM
   cd dashboard && npm audit --audit-level=high   # Node
   ```
   - HIGH or CRITICAL → run [patchVulnerability](patchVulnerability.md).
   - MODERATE → triage: bump now if cleanly upgradable, otherwise file a KOS-* ticket.
   - LOW → log only; bump on the next [weeklyMaintenance](../workflows/weeklyMaintenance.md).

3. **Image pass** (only when an image was just rebuilt):
   ```
   trivy image kosprin/<service>:<tag> --severity HIGH,CRITICAL --exit-code 1
   ```
   Non-zero exit → block the deploy until either (a) the base image is bumped or (b) the CVE is justified-pinned with a KOS-* link.

4. **Surface that touches auth or external IO** also gets a [threatModelFeature](threatModelFeature.md) pass before the PR opens.

5. **Record the result**. If anything was found, append a one-liner to [`../memory/decisions.md`](../memory/decisions.md): date, severity, decision, link to the fix or the deferred ticket.

## Done condition

- All three layers ran cleanly OR every finding has a triage action (fix landed, ticket filed, or pinned with rationale).
- The branch is safe to push by [securityAuditor](../agents/securityAuditor.md) standards.

## Common failures

- **`gitleaks` flags a long random string in a fixture** — likely false positive. Add to `.gitleaksignore` with `# fixture: <file>` reason.
- **`dependencyCheckAnalyze` says "no NVD data"** — first run downloads the database (~10 min). Second run is fast.
- **`trivy` flags a CVE with no fix available** — pin with `# pinned: CVE-XXXX no upstream fix; mitigated by <control>` and re-check on the next maintenance cycle.

## Composes with
- [securityAuditor](../agents/securityAuditor.md) — the role that owns this skill.
- [securityReview](../workflows/securityReview.md) — the workflow this slots into.
- [prePushChecklist](../workflows/prePushChecklist.md) — runs the secrets layer at minimum on every push.
