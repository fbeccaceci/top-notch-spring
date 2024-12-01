package com.iplastudio.boilerplate.features.security.jwt.exceptions

import com.iplastudio.boilerplate.exceptions.AppException
import org.springframework.http.HttpStatus

class InvalidRefreshTokenException: AppException(
    responseStatus = HttpStatus.UNAUTHORIZED,
    error = "invalid-refresh-token",
    message = "Invalid refresh token",
    details = "Invalid refresh token"
)