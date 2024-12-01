package com.iplastudio.boilerplate.features.users.exceptions

import com.iplastudio.boilerplate.exceptions.AppException
import org.springframework.http.HttpStatus

class VerificationTokenIsNotValidException: AppException(
    responseStatus = HttpStatus.UNPROCESSABLE_ENTITY,
    error = "verification-token-is-not-valid",
    message = "Verification token is not valid",
    details = "Verification token is not valid"
)