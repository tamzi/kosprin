---
name: messagingStack
description: Kafka with auto-create disabled; eventId is UUID v7; consumers dedupe; DLT per topic.
type: memory
---

# Messaging stack

Kafka 3.7 in KRaft mode (no Zookeeper). Topics are created explicitly by `kafka-init` in `docker-compose.yml`; `KAFKA_CFG_AUTO_CREATE_TOPICS_ENABLE` is `false` so a typo in a topic name fails fast.

Standing topics today: `videos`, `video-processed`, `notifications`, `analytics`. Topic naming is **kebab-case**; full conventions live in [`../../docs/tech/messaging.md`](../../docs/tech/messaging.md).

Every Kafka message carries an `eventId` (**UUID v7** — time-ordered, so lexicographic sort = chronological sort). Consumers **dedupe** on `eventId`. Every consumer routes poison messages to the topic's **DLT** (`<topic>.DLT`).

**Why:** auto-create silently masks producer typos and creates topics with default replication / partition settings that we don't want. UUID v7 was picked over v4 because the time-ordered prefix lets us range-query event stores cheaply. Forcing dedupe at every consumer means at-least-once delivery is fine — exactly-once is not required at the broker.

**How to apply:** to add a topic, follow [`../skills/addKafkaTopic.md`](../skills/addKafkaTopic.md). To produce a single event, follow [`../skills/kafkaEventPublish.md`](../skills/kafkaEventPublish.md). Always include `eventId` in the message header; always wire a DLT on the consumer side.
