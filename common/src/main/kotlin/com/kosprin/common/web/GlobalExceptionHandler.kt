package com.kosprin.common.web

import com.kosprin.common.error.ApiError
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(ApiError::class)
    fun handleApiError(ex: ApiError): ResponseEntity<ProblemDetail> {
        val detail = ProblemDetail.forStatusAndDetail(ex.status, ex.message)
        detail.setProperty("code", ex.code)
        detail.setProperty("correlationId", MDC.get(CorrelationIdFilter.MDC_KEY))
        return ResponseEntity.status(ex.status).body(detail)
    }

    @ExceptionHandler(Exception::class)
    fun handleUnexpected(
        ex: Exception,
        request: HttpServletRequest,
    ): ResponseEntity<ProblemDetail> {
        log.error("Unexpected error on ${request.method} ${request.requestURI}", ex)
        val detail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred")
        detail.setProperty("correlationId", MDC.get(CorrelationIdFilter.MDC_KEY))
        return ResponseEntity.internalServerError().body(detail)
    }
}
