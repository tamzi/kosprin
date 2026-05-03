package com.kosprin.common.kafka

import com.kosprin.common.event.EventHeaders
import com.kosprin.common.web.CorrelationIdFilter
import org.apache.kafka.clients.consumer.ConsumerInterceptor
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition
import org.slf4j.MDC

class KafkaCorrelationConsumerInterceptor : ConsumerInterceptor<Any, Any> {
    override fun onConsume(records: ConsumerRecords<Any, Any>): ConsumerRecords<Any, Any> {
        records.forEach { record ->
            record.headers().lastHeader(EventHeaders.CORRELATION_ID)?.let { header ->
                MDC.put(CorrelationIdFilter.MDC_KEY, String(header.value(), Charsets.UTF_8))
            }
        }
        return records
    }

    override fun onCommit(offsets: MutableMap<TopicPartition, OffsetAndMetadata>?) = Unit

    override fun close() = Unit

    override fun configure(configs: MutableMap<String, *>?) = Unit
}
