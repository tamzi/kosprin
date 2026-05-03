package com.kosprin.common.error

import org.springframework.http.HttpStatus

sealed class ApiError(
    val status: HttpStatus,
    override val message: String,
    val code: String,
) : RuntimeException(message)

class NotFoundError(
    resource: String,
    id: Any,
) : ApiError(
        HttpStatus.NOT_FOUND,
        "$resource '$id' not found",
        "NOT_FOUND",
    )

class ValidationError(
    field: String,
    reason: String,
) : ApiError(
        HttpStatus.BAD_REQUEST,
        "Validation failed for '$field': $reason",
        "VALIDATION_ERROR",
    )

class ConflictError(
    resource: String,
    reason: String,
) : ApiError(
        HttpStatus.CONFLICT,
        "$resource conflict: $reason",
        "CONFLICT",
    )

class ForbiddenError(
    reason: String = "Access denied",
) : ApiError(
        HttpStatus.FORBIDDEN,
        reason,
        "FORBIDDEN",
    )

class UnauthorizedError(
    reason: String = "Authentication required",
) : ApiError(
        HttpStatus.UNAUTHORIZED,
        reason,
        "UNAUTHORIZED",
    )
