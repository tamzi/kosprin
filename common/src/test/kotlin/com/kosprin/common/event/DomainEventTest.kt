package com.kosprin.common.event

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

class DomainEventTest {
    @Test
    fun `eventId is auto-generated when not provided`() {
        val e = DomainEvent(eventType = "VideoCreated", source = "metadata-service", payload = "x")
        e.eventId shouldNotBe null
        e.eventId.length shouldBe 36
    }

    @Test
    fun `schemaVersion defaults to 1`() {
        val e = DomainEvent(eventType = "T", source = "s", payload = Unit)
        e.schemaVersion shouldBe 1
    }

    @Test
    fun `correlationId defaults to null`() {
        val e = DomainEvent(eventType = "T", source = "s", payload = Unit)
        e.correlationId shouldBe null
    }

    @Test
    fun `payload is stored correctly`() {
        data class VideoPayload(
            val id: String,
        )
        val payload = VideoPayload("vid-42")
        val e = DomainEvent(eventType = "VideoCreated", source = "metadata-service", payload = payload)
        e.payload shouldBe payload
    }

    @Test
    fun `EventHeaders constants are non-blank`() {
        listOf(
            EventHeaders.CORRELATION_ID,
            EventHeaders.EVENT_SOURCE,
            EventHeaders.SCHEMA_VERSION,
            EventHeaders.EVENT_ID,
            EventHeaders.EVENT_TYPE,
        ).forEach { it.isBlank() shouldBe false }
    }
}
