---
name: openPullRequest
description: Branch → push (with explicit user approval) → gh pr create with the standard body template.
type: skill
uses: [branchNaming]
---

# Skill — Open a pull request

Branch → push (with explicit user approval) → `gh pr create` with the standard body. The push half of this skill requires the user to explicitly say "push" — see [`../rules/agentBehaviour.md`](../rules/agentBehaviour.md).

## Prerequisites

- The branch is up-to-date with `master` (rebase or merge per project convention; this repo prefers rebase for feature branches).
- All commits on the branch follow [commitConventions](../rules/commitConventions.md).
- The [prePushChecklist](../workflows/prePushChecklist.md) passed.
- The user has explicitly approved the push.

## Steps

1. **Survey the branch**, in parallel:
   - `git status` — clean tree?
   - `git log master..HEAD --oneline` — every commit that will land.
   - `git diff master...HEAD --stat` — total surface.
   - `gh auth status` — token is good.

2. **Draft the title.** Past tense, < 70 chars, mirrors the dominant commit subject. PR titles are a *summary*, not a verb list.

3. **Draft the body** using the template below. Bullets, not paragraphs.

4. **Push the branch** if it has no upstream: `git push -u origin <branch>`. Never `--force` or `--no-verify`.

5. **Create the PR**:
   ```
   gh pr create --title "<title>" --body "$(cat <<'EOF'
   ## Summary
   <1–3 bullets>

   ## Tickets
   - KOS-X.Y — <one-line>

   ## Test plan
   - [ ] <what was actually run>
   - [ ] <what reviewer should verify>

   ## Risk / rollout
   <one line — none / behind feature flag / requires migration>
   EOF
   )"
   ```

6. **Post the PR URL back to the user.** That is the deliverable.

7. **Do not auto-add reviewers, labels, or milestones** unless the user asked or there is a CODEOWNERS rule. Drive-by metadata noise hides the signal.

## Body template — sections

- **Summary** — what changed, framed for the reviewer.
- **Tickets** — `KOS-*` IDs covered by the PR.
- **Test plan** — what was actually verified locally + what the reviewer should run.
- **Risk / rollout** — none / feature flag / migration / breaking. Only fill in if non-trivial.

## Done condition

- `gh pr view --web` shows the PR with a populated body.
- CI is queued (don't wait for it; that's the [reviewer](../agents/reviewer.md)'s gate).
- The PR URL was returned to the user.

## Composes with
- [prePushChecklist](../workflows/prePushChecklist.md) — must pass before this skill runs.
- [reviewPullRequest](reviewPullRequest.md) — what the [reviewer](../agents/reviewer.md) does next.
- [featureDelivery](../workflows/featureDelivery.md) — the broader flow that contains this step.
