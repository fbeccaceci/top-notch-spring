package com.iplastudio.boilerplate.features.users.exceptions

import com.iplastudio.boilerplate.exceptions.AppException
import org.springframework.http.HttpStatus

class VerificationTokenNotFoundException: AppException(
    responseStatus = HttpStatus.NOT_FOUND,
    error = "verification-token-not-found",
    message = "Verification token not found",
    details = "Verification token not found"
)