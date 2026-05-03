---
name: bumpDependency
description: Bump one dep cleanly — manifest + lockfile + verify + correct commit shape.
type: skill
---

# Skill — Bump a single dependency

The atomic version of [checkDependencyUpdates](checkDependencyUpdates.md). Use this when bumping one dep deliberately (a CVE patch, a feature you need from a newer release, or a transitive constraint to lift).

For the periodic full sweep, use [checkDependencyUpdates](checkDependencyUpdates.md). For triaging a CVE specifically, use [patchVulnerability](patchVulnerability.md).

## Steps

1. **Confirm the latest stable version** — vendor's release page, not just `npm outdated` (which can lag by a few hours).
2. **Read the changelog** for everything between the current and target version. If there is a breaking section, plan to land the API changes in the same PR; the bump cannot ship "naked".
3. **Edit the version source**:
   - JVM: [`gradle/libs.versions.toml`](../../gradle/libs.versions.toml). One key per dep — never bump in a build script.
   - Dashboard: `dashboard/package.json`. Then regenerate the lockfile cleanly.
4. **Regenerate lockfiles cleanly** — never edit a lockfile by hand:
   ```
   # Node
   cd dashboard && rm package-lock.json && npm install --no-audit --no-fund

   # JVM (only if you're using a Gradle lockfile)
   ./gradlew dependencies --write-locks
   ```
5. **Verify**:
   - `./gradlew build` — passes.
   - `./gradlew test` — passes (or update tests in the same PR if behaviour legitimately changed).
   - `cd dashboard && npx tsc --noEmit && npm run build` — passes.
   - `npm audit` — 0 HIGH / 0 CRITICAL.
6. **Commit shape**:
   - `Bumped <dep> to <version>` — version + lockfile only.
   - `Adjusted <X> for <dep> <version>` — any code changes the bump forces.
   - One commit per `.md` if the changelog mention belongs in a doc.
7. **If you choose NOT to bump** (e.g. major version requires a JDK move) — annotate the version key with a `# pinned: <reason>` comment, including a KOS-* link if the bump is being deferred to a planned ticket. See [`agentBehaviour.md`](../rules/agentBehaviour.md).

## Done condition

- Lockfile is in sync with the manifest (no diff after a clean install).
- Build + tests + type-check + audit all pass.
- The bump and any forced code changes landed in the same PR (so reverting is one commit).

## Common failures

- **Peer-dep conflict** — `npm install` warns. Investigate before forcing with `--legacy-peer-deps`; the conflict often signals a breaking change you missed in the changelog.
- **Spring Boot starter bump** — bump `springBoot` in `libs.versions.toml`; the BOM transitively pulls everything else. Don't bump individual `spring-*` deps directly.
- **Transitive dep that won't budge** — find the parent that pins it (`./gradlew dependencyInsight`) and bump that instead.

## Composes with
- [checkDependencyUpdates](checkDependencyUpdates.md) — the broader sweep.
- [patchVulnerability](patchVulnerability.md) — when the bump is CVE-driven.
- [dependencyWarden](../agents/dependencyWarden.md) — owns the cycle.
