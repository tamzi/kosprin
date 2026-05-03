---
name: tagRelease
description: Create and push an annotated release tag after release gates pass and the user approves.
type: skill
---

# Skill - Tag Release

Use this only inside [releaseFlow](../workflows/releaseFlow.md).

## Steps

1. Confirm the release SHA, changelog entry, CI state, scans, and eval requirements.
2. Ask for explicit user approval before pushing any tag.
3. Create an annotated tag with the chosen version.
4. Push the tag without `--force` and record the release in [`../memory/decisions.md`](../memory/decisions.md).

## Done condition

- The tag exists remotely and points at the approved SHA.
