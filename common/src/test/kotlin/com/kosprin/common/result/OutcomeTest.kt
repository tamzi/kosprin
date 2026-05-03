package com.kosprin.common.result

import com.kosprin.common.error.NotFoundError
import com.kosprin.common.error.ValidationError
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class OutcomeTest {
    private val notFound = NotFoundError("Video", "abc-123")
    private val validationErr = ValidationError("title", "must not be blank")

    @Test
    fun `Success isSuccess and isFailure flags`() {
        val outcome = 42.asSuccess()
        outcome.isSuccess shouldBe true
        outcome.isFailure shouldBe false
    }

    @Test
    fun `Failure isSuccess and isFailure flags`() {
        val outcome = notFound.asFailure()
        outcome.isSuccess shouldBe false
        outcome.isFailure shouldBe true
    }

    @Test
    fun `getOrNull returns value on Success`() {
        "hello".asSuccess().getOrNull() shouldBe "hello"
    }

    @Test
    fun `getOrNull returns null on Failure`() {
        notFound.asFailure().getOrNull() shouldBe null
    }

    @Test
    fun `errorOrNull returns error on Failure`() {
        notFound.asFailure().errorOrNull() shouldBe notFound
    }

    @Test
    fun `errorOrNull returns null on Success`() {
        "ok".asSuccess().errorOrNull() shouldBe null
    }

    @Test
    fun `map transforms Success value`() {
        val result = 10.asSuccess().map { it * 2 }
        result.getOrNull() shouldBe 20
    }

    @Test
    fun `map passes Failure through unchanged`() {
        val result = notFound.asFailure().map { "never" }
        result.errorOrNull() shouldBe notFound
    }

    @Test
    fun `flatMap chains Success outcomes`() {
        val result = 5.asSuccess().flatMap { (it + 1).asSuccess() }
        result.getOrNull() shouldBe 6
    }

    @Test
    fun `flatMap short-circuits on Failure`() {
        val result = notFound.asFailure().flatMap { "never".asSuccess() }
        result.errorOrNull() shouldBe notFound
    }

    @Test
    fun `flatMap propagates inner Failure`() {
        val result = 5.asSuccess().flatMap { validationErr.asFailure() }
        result.errorOrNull() shouldBe validationErr
    }

    @Test
    fun `onSuccess invoked for Success`() {
        var called = false
        "x".asSuccess().onSuccess { called = true }
        called shouldBe true
    }

    @Test
    fun `onSuccess not invoked for Failure`() {
        var called = false
        notFound.asFailure().onSuccess { called = true }
        called shouldBe false
    }

    @Test
    fun `onFailure invoked for Failure`() {
        var captured: Any? = null
        notFound.asFailure().onFailure { captured = it }
        captured shouldBe notFound
    }

    @Test
    fun `onFailure not invoked for Success`() {
        var called = false
        "ok".asSuccess().onFailure { called = true }
        called shouldBe false
    }

    @Test
    fun `Success wraps correct type`() {
        "value".asSuccess().shouldBeInstanceOf<Outcome.Success<String>>()
    }

    @Test
    fun `Failure wraps correct type`() {
        notFound.asFailure().shouldBeInstanceOf<Outcome.Failure>()
    }
}
