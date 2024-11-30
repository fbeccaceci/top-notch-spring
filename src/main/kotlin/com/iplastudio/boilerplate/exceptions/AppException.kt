package com.iplastudio.boilerplate.exceptions

import org.springframework.http.HttpStatus

open class AppException(
    val responseStatus: HttpStatus,
    val error: String,
    override val message: String,
    val details: String,
    cause: Throwable? = null
): RuntimeException(message, cause)