package com.kosprin.common.kafka

import com.kosprin.common.event.EventHeaders
import com.kosprin.common.web.CorrelationIdFilter
import org.apache.kafka.clients.producer.ProducerInterceptor
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.slf4j.MDC
import java.util.UUID

class KafkaCorrelationProducerInterceptor : ProducerInterceptor<Any, Any> {
    override fun onSend(record: ProducerRecord<Any, Any>): ProducerRecord<Any, Any> {
        val correlationId = MDC.get(CorrelationIdFilter.MDC_KEY) ?: UUID.randomUUID().toString()
        record.headers().add(EventHeaders.CORRELATION_ID, correlationId.toByteArray(Charsets.UTF_8))
        return record
    }

    override fun onAcknowledgement(
        metadata: RecordMetadata?,
        exception: Exception?,
    ) = Unit

    override fun close() = Unit

    override fun configure(configs: MutableMap<String, *>?) = Unit
}
