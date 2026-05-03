package com.kosprin.common.error

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class ApiErrorTest {
    @Test
    fun `NotFoundError has correct status and message`() {
        val err = NotFoundError("Video", "vid-1")
        err.status shouldBe HttpStatus.NOT_FOUND
        err.code shouldBe "NOT_FOUND"
        err.message shouldBe "Video 'vid-1' not found"
    }

    @Test
    fun `ValidationError has correct status and message`() {
        val err = ValidationError("title", "must not be blank")
        err.status shouldBe HttpStatus.BAD_REQUEST
        err.code shouldBe "VALIDATION_ERROR"
        err.message shouldBe "Validation failed for 'title': must not be blank"
    }

    @Test
    fun `ConflictError has correct status and message`() {
        val err = ConflictError("Video", "slug already taken")
        err.status shouldBe HttpStatus.CONFLICT
        err.code shouldBe "CONFLICT"
        err.message shouldBe "Video conflict: slug already taken"
    }

    @Test
    fun `ForbiddenError uses default message`() {
        val err = ForbiddenError()
        err.status shouldBe HttpStatus.FORBIDDEN
        err.code shouldBe "FORBIDDEN"
        err.message shouldBe "Access denied"
    }

    @Test
    fun `ForbiddenError accepts custom message`() {
        val err = ForbiddenError("Moderators only")
        err.message shouldBe "Moderators only"
    }

    @Test
    fun `UnauthorizedError uses default message`() {
        val err = UnauthorizedError()
        err.status shouldBe HttpStatus.UNAUTHORIZED
        err.code shouldBe "UNAUTHORIZED"
        err.message shouldBe "Authentication required"
    }

    @Test
    fun `ApiError subtypes are RuntimeException`() {
        val err: RuntimeException = NotFoundError("X", 1)
        err.message shouldBe "X '1' not found"
    }
}
