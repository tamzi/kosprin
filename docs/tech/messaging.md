# Messaging (Kafka)

Async backbone of the system. All cross-service communication that does not need a synchronous reply goes through Kafka.

## Topics

| Topic              | Producers              | Consumers                                  | Purpose                                    | Partition key |
|--------------------|------------------------|--------------------------------------------|--------------------------------------------|---------------|
| `videos`           | metadata-service       | video-service, search-service, feed-service | A new video record was created.           | `videoId`     |
| `video-processed`  | video-service          | metadata-service                            | Processing finished for a given video.    | `videoId`     |
| `notifications`    | feed-service           | notification-service                        | Push a notification to a user.            | `userId`      |
| `analytics`        | feed-service, search-service | analytics-service                     | Operational/usage events for rollups.     | `userId`      |
| `*.DLT`            | (consumers)            | ops only                                    | Dead-letter for poison messages.          | original key  |

## Conventions
- **Serialization**: target Avro/Protobuf with a schema registry (KOS-S2). Until then, JSON with a `schemaVersion` header.
- **Headers**: `correlationId`, `eventId`, `source`, `schemaVersion`. Producers must set them; consumers must propagate.
- **Idempotency**: every event carries an `eventId` (UUID v7). Consumers dedupe on `(eventId, consumerGroup)` for at-least-once safety.
- **Ordering**: only guaranteed within a partition. Choose partition keys so order matters within the key (e.g. one video's lifecycle events stay in one partition).
- **Retries**: 3 in-line attempts with exponential backoff, then to the topic's DLT. DLT consumers are operator-driven, not automatic.

## Schema evolution
Backward-compatible changes only on existing topics. Breaking changes require a new topic (`videos.v2`) plus a dual-write migration window.

## Local dev
Topics are auto-created by the init script in [`localDevelopment.md`](localDevelopment.md). In production, topics are pre-created with explicit partition counts and retention.
