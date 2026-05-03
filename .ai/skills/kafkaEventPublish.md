---
name: kafkaEventPublish
description: Publish Kafka events with standard headers, schema versioning, and idempotency metadata.
type: skill
---

# Skill - Kafka Event Publish

Use this when adding or changing a producer.

## Steps

1. Confirm the topic exists in [`../../docs/tech/messaging.md`](../../docs/tech/messaging.md).
2. Use shared event headers from `common/`; do not hand-roll correlation or event IDs.
3. Include schema version, source service, correlation ID, and UUID v7 `eventId`.
4. Add producer-side contract coverage where the event becomes a cross-service dependency.

## Done condition

- Consumers can dedupe the event and trace it across services.
