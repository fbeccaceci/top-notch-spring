package com.iplastudio.boilerplate.features.users.exceptions

import com.iplastudio.boilerplate.exceptions.AppException
import org.springframework.http.HttpStatus

class UserAlreadyActiveException: AppException(
    responseStatus = HttpStatus.UNPROCESSABLE_ENTITY,
    error = "user-already-active",
    message = "The user is already active",
    details = "The user is already active"
)