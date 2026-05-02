package com.kosprin.common.result

import com.kosprin.common.error.ApiError

sealed class Outcome<out T> {
    data class Success<T>(
        val value: T,
    ) : Outcome<T>()

    data class Failure(
        val error: ApiError,
    ) : Outcome<Nothing>()

    val isSuccess: Boolean get() = this is Success
    val isFailure: Boolean get() = this is Failure

    fun getOrNull(): T? = (this as? Success)?.value

    fun errorOrNull(): ApiError? = (this as? Failure)?.error

    fun <R> map(transform: (T) -> R): Outcome<R> =
        when (this) {
            is Success -> Success(transform(value))
            is Failure -> this
        }

    fun <R> flatMap(transform: (T) -> Outcome<R>): Outcome<R> =
        when (this) {
            is Success -> transform(value)
            is Failure -> this
        }

    fun onSuccess(action: (T) -> Unit): Outcome<T> =
        also {
            if (this is Success) action(value)
        }

    fun onFailure(action: (ApiError) -> Unit): Outcome<T> =
        also {
            if (this is Failure) action(error)
        }
}

fun <T> T.asSuccess(): Outcome<T> = Outcome.Success(this)

fun ApiError.asFailure(): Outcome<Nothing> = Outcome.Failure(this)
