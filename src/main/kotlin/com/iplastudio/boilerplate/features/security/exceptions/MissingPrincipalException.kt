package com.iplastudio.boilerplate.features.security.exceptions

import com.iplastudio.boilerplate.exceptions.AppException
import org.springframework.http.HttpStatus

class MissingPrincipalException : AppException(
    responseStatus = HttpStatus.UNAUTHORIZED,
    error = "missing-principal",
    message = "Missing principal",
    details = "The principal in security context is null while it was expected to be not null"
)