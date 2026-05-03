# Observability

Three pillars: **metrics**, **logs**, **traces**. Every service must emit all three before being considered production-ready (KOS-13).

## Metrics
- Each service runs Spring Boot Actuator with the Micrometer Prometheus registry.
- Prometheus scrapes `/actuator/prometheus` every 15s.
- Required metrics per service:
  - HTTP: `http.server.requests` (latency, count, error rate per route).
  - Kafka: consumer lag, producer send rate/error rate.
  - JVM: heap, GC pause, thread count.
  - Custom domain counters (e.g. `videos.created.total`).

## Dashboards (Grafana)
Provisioned from version-controlled JSON under `infra/grafana/`. Three baseline dashboards:
1. **Per-service** — RED metrics (rate, errors, duration).
2. **System overview** — Kafka topic lag, queue depth, end-to-end latency from `videos` to `video-processed`.
3. **Business** — videos/day, search QPS, feed reads.

## Logs
- Structured JSON via Logback's `LogstashEncoder`.
- Required fields: `timestamp`, `level`, `service`, `traceId`, `spanId`, `userId` (when known), `message`.
- Local: stdout. Production: shipped to Loki or ELK.
- No PII in logs. Audit-style events go through the `analytics` Kafka topic instead.

## Traces
- OpenTelemetry Java agent attached to every service (no code changes).
- Trace context propagated over HTTP and Kafka headers (`traceparent`, `tracestate`).
- Backend: Tempo (preferred) or Jaeger. Sample rate 10% in prod, 100% in dev.

## Health checks
- `/actuator/health` is the source of truth for kubelet liveness/readiness.
- Liveness checks are cheap (process up). Readiness checks include downstream dependencies (DB, Kafka, ES).
- The gateway aggregates downstream health into `/actuator/health/services` for dashboards.

## Alerts (initial set)
- Kafka consumer lag > 10k for 5 minutes.
- HTTP 5xx rate > 1% on any service for 5 minutes.
- p95 latency > 1s on any user-facing endpoint for 5 minutes.
- Pod CrashLoopBackOff.
