package com.iplastudio.boilerplate.features.users.exceptions

import com.iplastudio.boilerplate.exceptions.AppException
import org.springframework.http.HttpStatus

class TokenRefreshFailedException(cause: Throwable?): AppException(
    responseStatus = HttpStatus.UNAUTHORIZED,
    error = "token refresh failed",
    message = "It was not possible to refresh the token",
    details = "It was not possible to refresh the token",
    cause = cause
)