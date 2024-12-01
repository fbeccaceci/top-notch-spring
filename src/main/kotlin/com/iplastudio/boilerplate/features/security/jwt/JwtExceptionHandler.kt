package com.iplastudio.boilerplate.features.security.jwt

import com.iplastudio.boilerplate.exceptions.ApiError
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SignatureException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class JwtExceptionHandler {

    @ExceptionHandler
    fun handleMalformedJwtException(e: MalformedJwtException): ResponseEntity<ApiError> {
        val error = ApiError(
            error = "unauthorized",
            message = "Malformed JWT",
            details = "Malformed JWT"
        )

        return ResponseEntity(error, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler
    fun handleExpiredJwtException(e: ExpiredJwtException): ResponseEntity<ApiError> {
        val error = ApiError(
            error = "unauthorized",
            message = "Expired JWT",
            details = "Expired JWT"
        )

        return ResponseEntity(error, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler
    fun handleSignatureException(e: SignatureException): ResponseEntity<ApiError> {
        val error = ApiError(
            error = "unauthorized",
            message = "Signature not valid",
            details = "Signature not valid"
        )

        return ResponseEntity(error, HttpStatus.UNAUTHORIZED)
    }

}