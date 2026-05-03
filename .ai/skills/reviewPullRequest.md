---
name: reviewPullRequest
description: Standard PR review walkthrough — architecture, style, commit shape, security, CI, decision.
type: skill
---

# Skill — Review a pull request

The standard PR review walkthrough. Drives the [reviewer](../agents/reviewer.md) agent's checklist.

## Steps

1. **Read the PR description first.** If the body doesn't say what the PR does, ask before reading code — a fuzzy description usually predicts a fuzzy diff.
2. **Pull the diff**: `gh pr diff <number>` and `gh pr view <number> --json files,additions,deletions,commits`.
3. **Walk the diff once for shape** — what got added, what got removed, where the "gravity" is. Don't comment yet; you're orienting.
4. **Walk the diff again with the rule modules open**:
   - [architecture](../rules/architecture.md) — module boundary respected? sync vs async right? idempotency keys present?
   - [codeStyle](../rules/codeStyle.md) — Kotlin/TS conventions? `!!` justified? no `any` without comment?
   - [commitConventions](../rules/commitConventions.md) — every commit atomic, past-tense, no body, no bundled docs?
   - [documentationConventions](../rules/documentationConventions.md) — no code in `.md`, modules under 100 lines, camelCase?
5. **Run the security pass** for any change that touches: auth, an external surface, deserialisation, file I/O, child processes, secrets, dependencies. Hand off to [securityAuditor](../agents/securityAuditor.md) using [securityReview](../workflows/securityReview.md). Do not approve until they return.
6. **Run the test plan locally** if the PR touches a code path the test suite does not exercise. Type-checks and unit tests are not enough for UI changes — exercise it in the browser.
7. **Check CI**: `gh pr checks <number>`. Failed CI = request changes; do not approve "pending CI".
8. **Comment in priority order**: blockers first, suggestions second, nits last and explicitly labelled `nit:`.
9. **Decision**:
   - **Approve** — only if every checklist item passes.
   - **Request changes** — concrete, citing the rule line.
   - **Comment** — for "I want to discuss" without blocking.

## Refuse to approve when

- Commits are not atomic / wrong tense / bundled docs — blocks merge by repo convention.
- An external surface or auth path changed without a security pass.
- Tests are missing for a behaviour change.
- A new dependency was added without going through [bumpDependency](bumpDependency.md) (lockfile + audit + justification).
- Module README grew past 100 lines or a code snippet appeared in any `.md`.

## Done condition

- The PR has an explicit decision (`approve`, `request changes`, or substantive `comment`).
- Every blocker comment cites a specific rule or file:line.
- The user / author has been told what to fix to unblock approval.

## Composes with
- [reviewer](../agents/reviewer.md) — the role that runs this skill.
- [securityReview](../workflows/securityReview.md) — escalation when the diff has a security surface.
- [openPullRequest](openPullRequest.md) — what the author did to land here.
