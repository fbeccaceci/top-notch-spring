package com.iplastudio.boilerplate.exceptions

import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.NoHandlerFoundException

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler
    fun handleException(e: Exception): ResponseEntity<ApiError> {
        log.warn("A runtime exception occurred!", e)

        val error = ApiError(
            error = "unknown-error",
            message = "Unknown server error",
            details = e.message ?: ""
        )

        return ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<ApiDataValidationError> {
        val fields = e.bindingResult.allErrors
            .map {
                if (it is FieldError) {
                    return@map ApiDataValidationError.FailedField(it.field, it.defaultMessage)
                }

                return@map null
            }
            .filterNotNull()
            .toList()

        val error = ApiDataValidationError(
            error = "validation-failed",
            message = "Provided data is not valid",
            details = "Check that the data provided to the api was correct",
            failedFields = fields
        )

        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleAppException(e: AppException): ResponseEntity<ApiError> {
        val error = ApiError(
            error = e.error,
            message = e.message,
            details = e.details
        )

        return ResponseEntity(error, e.responseStatus)
    }

    @ExceptionHandler
    fun handleNoHandlerFoundException(e: NoHandlerFoundException): ResponseEntity<ApiError> {
        val error = ApiError(
            error = "no-handler-found",
            message = "This route does not exist",
            details = "This route does not exist"
        )

        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

}