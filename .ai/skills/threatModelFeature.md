---
name: threatModelFeature
description: Run a lightweight STRIDE review for a new external or security-sensitive surface.
type: skill
---

# Skill - Threat Model Feature

Use this before merging auth, external endpoint, upload, deserialisation, egress, or admin changes.

## Steps

1. State the asset, actors, trust boundary, and data flow.
2. Walk STRIDE categories and list credible threats only.
3. Assign severity and mitigation for each medium-or-higher threat.
4. Record accepted residual risk in [`../memory/decisions.md`](../memory/decisions.md).

## Done condition

- Medium-or-higher threats have a mitigation, ticket, or accepted-risk entry.
