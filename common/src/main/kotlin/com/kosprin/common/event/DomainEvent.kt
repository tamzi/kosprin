package com.kosprin.common.event

import java.time.Instant
import java.util.UUID

data class DomainEvent<T>(
    val eventId: String = UUID.randomUUID().toString(),
    val eventType: String,
    val source: String,
    val schemaVersion: Int = 1,
    val correlationId: String? = null,
    val occurredAt: Instant = Instant.now(),
    val payload: T,
)
