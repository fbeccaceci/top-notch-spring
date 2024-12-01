package com.iplastudio.boilerplate.features.users.exceptions

import com.iplastudio.boilerplate.exceptions.AppException
import org.springframework.http.HttpStatus

class UsernameOrEmailAlreadyInUseException: AppException(
    responseStatus = HttpStatus.CONFLICT,
    error = "username-or-email-already-in-use",
    message = "Username or email already in use",
    details = "Username or email already in use"
)