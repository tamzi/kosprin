# Skill — Check dependency updates

Run this before every push, and any time a dependency is touched. The rule that drives it lives in [`agents/rules/agentBehaviour.md`](../rules/agentBehaviour.md) ("Always — use the latest stable versions").

## JVM (Gradle)

The `com.github.ben-manes.versions` plugin is wired at the root. To list outdated deps:

```
./gradlew dependencyUpdates
```

Output buckets each dependency:
- **outdated** — bump these.
- **current** — at latest.
- **exceeded** — local version is newer than the last known release; usually a cache artefact, leave alone.
- **unresolved** — dependency could not be resolved; investigate, do not silently bump.

For Spring Boot starters, bump `springBoot` in [`gradle/libs.versions.toml`](../../gradle/libs.versions.toml) — the BOM controls all of them.

## Node (dashboard)

```
cd dashboard && npm outdated
```

Then bump versions in `package.json`, regenerate the lockfile cleanly:

```
rm package-lock.json && npm install --no-audit --no-fund
npm audit
npx tsc --noEmit
```

`npm audit` must report **0 vulnerabilities** before push. `tsc --noEmit` must pass.

## After major bumps

Major-version bumps frequently include breaking changes. Verify after each set of bumps:

| Stack | Verification                                              |
|-------|-----------------------------------------------------------|
| JVM   | `./gradlew build` and `./gradlew test`                    |
| Node  | `npx tsc --noEmit`, `npm audit`, and `npm run build` if applicable |

Fix breakages in the same commit batch. Do not push a "bumped X" commit that breaks the build.

## Documenting non-upgrades

If you choose **not** to upgrade something — for example a major version that requires significant refactoring, or a transitive dep pinned by an upstream constraint — add a comment so the next agent does not waste cycles re-trying the bump:

```toml
# gradle/libs.versions.toml
# pinned: Spring Boot 3.4 requires Java 17+; bump deferred until KOS-17 picks JDK target
springBoot = "3.3.5"
```

```json
// package.json
"react": "18.3.1",  // pinned: MUI 5 peer dep range
```

## When to skip the check

Never. Even doc-only PRs should run it — it takes seconds and catches drift early. The only acceptable shortcut is when the previous push on the same day already ran it.
