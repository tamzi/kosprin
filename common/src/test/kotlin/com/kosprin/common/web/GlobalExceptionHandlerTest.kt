package com.kosprin.common.web

import com.kosprin.common.error.ConflictError
import com.kosprin.common.error.NotFoundError
import com.kosprin.common.error.ValidationError
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class GlobalExceptionHandlerTest {
    private val handler = GlobalExceptionHandler()

    @Test
    fun `handleApiError returns NOT_FOUND status for NotFoundError`() {
        val response = handler.handleApiError(NotFoundError("Video", "v1"))
        response.statusCode shouldBe HttpStatus.NOT_FOUND
    }

    @Test
    fun `handleApiError returns BAD_REQUEST status for ValidationError`() {
        val response = handler.handleApiError(ValidationError("field", "bad"))
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
    }

    @Test
    fun `handleApiError returns CONFLICT status for ConflictError`() {
        val response = handler.handleApiError(ConflictError("Video", "duplicate"))
        response.statusCode shouldBe HttpStatus.CONFLICT
    }

    @Test
    fun `handleApiError sets error code in problem detail`() {
        val response = handler.handleApiError(NotFoundError("Tag", "t1"))
        response.body!!.properties!!["code"] shouldBe "NOT_FOUND"
    }

    @Test
    fun `handleUnexpected returns INTERNAL_SERVER_ERROR`() {
        val request = mockk<HttpServletRequest>(relaxed = true)
        every { request.method } returns "POST"
        every { request.requestURI } returns "/api/videos"
        val response = handler.handleUnexpected(RuntimeException("boom"), request)
        response.statusCode shouldBe HttpStatus.INTERNAL_SERVER_ERROR
    }

    @Test
    fun `handleUnexpected body detail is set`() {
        val request = mockk<HttpServletRequest>(relaxed = true)
        every { request.method } returns "GET"
        every { request.requestURI } returns "/api/search"
        val response = handler.handleUnexpected(Exception("fail"), request)
        response.body!!.detail shouldBe "An unexpected error occurred"
    }
}
