---
name: updateAgentMemory
description: Promote durable cross-agent facts into `.ai/memory/`.
type: skill
---

# Skill - Update Agent Memory

Use this when a fact will matter to future assistants and is not obvious from code or git history.

## Steps

1. Verify the fact against current code or docs.
2. Add or update the narrowest memory entry.
3. Update [`../memory/memoryIndex.md`](../memory/memoryIndex.md).
4. Remove stale memory as soon as it becomes wrong.

## Done condition

- Memory has one source of truth and links to the evidence.
