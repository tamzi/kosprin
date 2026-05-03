---
name: writeAdr
description: Capture an architecture decision under docs/adr/ with the standard template.
type: skill
---

# Skill — Write an ADR

Capture an architecture decision under `docs/adr/`. ADRs exist so that a future contributor (human or AI) can reconstruct *why* a non-obvious choice was made without reading the original PR thread.

## When to write one

- The decision is hard or expensive to reverse (datastore choice, framework swap, RPC protocol).
- The decision affects more than one service or module.
- A reasonable reader, six months from now, would ask "why did we do it this way?".

If the decision is local to one file or trivially reversible, skip the ADR — a code comment is enough.

## Steps

1. **Pick the next number.** `ls docs/adr/` and use the next zero-padded integer (e.g. `0007-`).
2. **Filename**: `NNNN-shortKebabTitle.md`. ADR filenames are the one camelCase exception — kebab-case is the ADR convention. Keep the title under six words.
3. **Use the standard sections** (template below). Do not invent new sections — uniformity is the point.
4. **Status starts at `Proposed`.** Promote to `Accepted` only after the user (or the relevant agent role) signs off.
5. **Link from the source-of-truth doc.** Add a one-line reference in [`docs/tech/systemdesign.md`](../../docs/tech/systemdesign.md) or the relevant module README. ADRs that nothing links to die.
6. **Update [`../memory/decisions.md`](../memory/decisions.md)** with a one-line entry pointing at the new ADR.

## Template (paste into the new file)

```
# NNNN — Title

- Status: Proposed | Accepted | Superseded by ADR-XXXX
- Date: YYYY-MM-DD

## Context
What forces are at play? What constraint or pain is driving this decision?

## Decision
The choice, stated as a single sentence the reader can quote.

## Consequences
What becomes easier. What becomes harder. What we now own that we did not before.

## Alternatives considered
Each alternative gets one sentence on why it was rejected.
```

## Done condition

- The ADR exists with status `Proposed` or `Accepted`.
- One commit: `Added ADR-NNNN <title>`.
- A separate commit adds the link to `systemdesign.md` or the module README.
- A separate commit adds the entry to `memory/decisions.md`.

(Three commits, because three different `.md` files. The [commit conventions](../rules/commitConventions.md) forbid bundling.)

## Composes with
- [architect](../agents/architect.md) — owns whether an ADR is required.
- [featureDelivery](../workflows/featureDelivery.md) — design step.
