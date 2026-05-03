---
name: inspectKafkaTopic
description: Inspect Kafka topics, offsets, and dead-letter traffic in the local stack.
type: skill
---

# Skill - Inspect Kafka Topic

Use this when debugging producer, consumer, or DLT behaviour.

## Steps

1. Confirm the topic name from [`../../docs/tech/messaging.md`](../../docs/tech/messaging.md).
2. List topics and describe the target topic.
3. Consume a bounded sample from the target topic or its DLT.
4. Check consumer group offsets when lag or replay is suspected.

## Done condition

- The topic, sample payload shape, and offset state are known.
