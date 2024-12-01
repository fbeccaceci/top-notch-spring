package com.iplastudio.boilerplate.features.users.exceptions

import com.iplastudio.boilerplate.exceptions.AppException
import org.springframework.http.HttpStatus

class UserNotEnabledException: AppException(
    responseStatus = HttpStatus.UNAUTHORIZED,
    error = "user-not-enabled",
    message = "User is not enabled",
    details = "User is not enabled"
)