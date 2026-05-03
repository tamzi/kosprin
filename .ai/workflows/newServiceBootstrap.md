---
name: newServiceBootstrap
description: End-to-end flow for adding a new service module and making it runnable.
type: workflow
uses: [architect, implementer, addNewService, runTests, updateSystemDesign]
---

# Workflow - New Service Bootstrap

## Trigger

A new service boundary has been accepted by the architect.

## Owner

[architect](../agents/architect.md) owns the boundary. [implementer](../agents/implementer.md) owns the scaffold.

## Steps

1. Confirm the service responsibility and data ownership.
2. Run [addNewService](../skills/addNewService.md).
3. Wire local runtime config and docs.
4. Run [runTests](../skills/runTests.md) for the new module.
5. Update system docs through [updateSystemDesign](../skills/updateSystemDesign.md).

## Done condition

- The service is included in Gradle, documented, testable, and represented in the architecture docs.
