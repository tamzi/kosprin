package com.kosprin.common.web

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import jakarta.servlet.FilterChain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.slf4j.MDC
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse

class CorrelationIdFilterTest {
    private val filter = CorrelationIdFilter()

    @AfterEach
    fun clearMdc() = MDC.clear()

    @Test
    fun `propagates existing correlation id from request header`() {
        val request = MockHttpServletRequest().apply { addHeader(CorrelationIdFilter.HEADER, "existing-id") }
        val response = MockHttpServletResponse()
        val chain = MockFilterChain()

        filter.doFilter(request, response, chain)

        response.getHeader(CorrelationIdFilter.HEADER) shouldBe "existing-id"
    }

    @Test
    fun `generates correlationId when header is absent`() {
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val chain = MockFilterChain()

        filter.doFilter(request, response, chain)

        response.getHeader(CorrelationIdFilter.HEADER) shouldNotBe null
        response.getHeader(CorrelationIdFilter.HEADER)!!.length shouldBe 36
    }

    @Test
    fun `MDC key is cleared after filter chain completes`() {
        val request = MockHttpServletRequest().apply { addHeader(CorrelationIdFilter.HEADER, "corr-id") }
        val response = MockHttpServletResponse()
        val chain = MockFilterChain()

        filter.doFilter(request, response, chain)

        MDC.get(CorrelationIdFilter.MDC_KEY) shouldBe null
    }

    @Test
    fun `MDC contains correlationId during filter chain execution`() {
        val request = MockHttpServletRequest().apply { addHeader(CorrelationIdFilter.HEADER, "during-id") }
        val response = MockHttpServletResponse()
        var capturedMdcValue: String? = null
        val chain = FilterChain { _, _ -> capturedMdcValue = MDC.get(CorrelationIdFilter.MDC_KEY) }

        filter.doFilter(request, response, chain)

        capturedMdcValue shouldBe "during-id"
    }
}
