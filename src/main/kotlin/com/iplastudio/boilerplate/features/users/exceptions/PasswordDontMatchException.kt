package com.iplastudio.boilerplate.features.users.exceptions

import com.iplastudio.boilerplate.exceptions.AppException
import org.springframework.http.HttpStatus

class PasswordDontMatchException: AppException(
    responseStatus = HttpStatus.UNAUTHORIZED,
    error = "password-dont-match",
    message = "Password don't match",
    details = "Password don't match"
)