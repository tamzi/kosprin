package com.kosprin.common.kafka

import com.kosprin.common.event.EventHeaders
import com.kosprin.common.web.CorrelationIdFilter
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.header.internals.RecordHeader
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.slf4j.MDC

class KafkaCorrelationInterceptorTest {
    @AfterEach
    fun clearMdc() = MDC.clear()

    // ── Producer ─────────────────────────────────────────────────────────────

    @Test
    fun `producer interceptor adds correlationId from MDC to record headers`() {
        MDC.put(CorrelationIdFilter.MDC_KEY, "prod-corr-id")
        val record = ProducerRecord<Any, Any>("videos", "payload")

        val result = KafkaCorrelationProducerInterceptor().onSend(record)

        val header = result.headers().lastHeader(EventHeaders.CORRELATION_ID)
        header shouldNotBe null
        String(header!!.value(), Charsets.UTF_8) shouldBe "prod-corr-id"
    }

    @Test
    fun `producer interceptor generates correlationId when MDC is empty`() {
        MDC.clear()
        val record = ProducerRecord<Any, Any>("videos", "payload")

        val result = KafkaCorrelationProducerInterceptor().onSend(record)

        val header = result.headers().lastHeader(EventHeaders.CORRELATION_ID)
        header shouldNotBe null
        String(header!!.value(), Charsets.UTF_8).length shouldBe 36
    }

    @Test
    fun `producer interceptor no-ops on lifecycle callbacks`() {
        val interceptor = KafkaCorrelationProducerInterceptor()
        interceptor.onAcknowledgement(null, null)
        interceptor.close()
        interceptor.configure(mutableMapOf<String, Any>())
    }

    // ── Consumer ─────────────────────────────────────────────────────────────

    @Test
    fun `consumer interceptor propagates correlationId header to MDC`() {
        val record = ConsumerRecord<Any, Any>("videos", 0, 0L, "key", "value")
        record.headers().add(RecordHeader(EventHeaders.CORRELATION_ID, "cons-corr-id".toByteArray(Charsets.UTF_8)))
        val records = ConsumerRecords<Any, Any>(mapOf(TopicPartition("videos", 0) to listOf(record)))

        KafkaCorrelationConsumerInterceptor().onConsume(records)

        MDC.get(CorrelationIdFilter.MDC_KEY) shouldBe "cons-corr-id"
    }

    @Test
    fun `consumer interceptor handles record with no correlationId header`() {
        val record = ConsumerRecord<Any, Any>("videos", 0, 0L, "key", "value")
        val records = ConsumerRecords<Any, Any>(mapOf(TopicPartition("videos", 0) to listOf(record)))

        KafkaCorrelationConsumerInterceptor().onConsume(records)

        MDC.get(CorrelationIdFilter.MDC_KEY) shouldBe null
    }

    @Test
    fun `consumer interceptor no-ops on lifecycle callbacks`() {
        val interceptor = KafkaCorrelationConsumerInterceptor()
        interceptor.onCommit(mutableMapOf<TopicPartition, org.apache.kafka.clients.consumer.OffsetAndMetadata>())
        interceptor.close()
        interceptor.configure(mutableMapOf<String, Any>())
    }
}
