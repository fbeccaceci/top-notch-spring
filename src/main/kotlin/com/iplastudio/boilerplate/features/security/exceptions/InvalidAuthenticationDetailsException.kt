package com.iplastudio.boilerplate.features.security.exceptions

import com.iplastudio.boilerplate.exceptions.AppException
import org.springframework.http.HttpStatus

class InvalidAuthenticationDetailsException(message: String? = null, details: String? = null) : AppException(
    responseStatus = HttpStatus.UNAUTHORIZED,
    error = "invalid-authentication-details",
    message = message ?: "Invalid authentication details",
    details = details ?: "Invalid authentication details"
)