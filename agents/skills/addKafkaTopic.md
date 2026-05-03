# Skill — Add a Kafka topic

Use this when a new event type needs to flow between services.

## 1. Justify it
Before adding a topic, confirm an existing topic does not already cover the use case. Splitting events that should travel together is harder to undo than collapsing topics later.

Decision checks:
- Is the new event a **lifecycle change** of an existing entity? → reuse the existing topic with a versioned schema.
- Is it a **new domain concern** (e.g. moderation events vs content events)? → new topic.
- Is the consumer set entirely different and the retention policy different? → new topic.

## 2. Update the docs first
Add the topic to the table in [`docs/tech/messaging.md`](../../docs/tech/messaging.md). Include:
- Producers and consumers.
- Partition key (must support per-key ordering for the consumers that depend on order).
- Retention policy if non-default.
- Whether a DLT (`<topic>.DLT`) is needed.

## 3. Producer
- Set the standard headers: `correlationId`, `eventId` (UUID v7), `source`, `schemaVersion`.
- Use the producer wrapper from `common/` rather than a raw `KafkaTemplate`.
- For services that also persist to Postgres, use the outbox pattern (KOS-21) once it is available; until then, a TODO comment is acceptable but the gap must be flagged.

## 4. Consumer
- Idempotent by construction — dedupe on `eventId`.
- Configure retries (3 attempts, exponential backoff) and a DLT.
- Manual ack only after side effects commit.

## 5. Local stack
Update the topic init script (KOS-3.4) to create the topic with the right partition count and replication factor.

## 6. Tests
- Producer test: verify the message is sent with the required headers.
- Consumer test: verify dedupe and DLT routing on poison messages.
- Use `spring-kafka-test`'s `EmbeddedKafka` for unit-level tests; Testcontainers for integration.

## 7. Commit split
1. `Added <topic> to messaging documentation`
2. `Added <topic> producer in <service>`
3. `Added <topic> consumer in <service>`
4. `Added <topic> init in local stack`
