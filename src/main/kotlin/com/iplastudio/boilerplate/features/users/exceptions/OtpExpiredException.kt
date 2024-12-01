package com.iplastudio.boilerplate.features.users.exceptions

import com.iplastudio.boilerplate.exceptions.AppException
import org.springframework.http.HttpStatus

class OtpExpiredException: AppException(
    responseStatus = HttpStatus.UNAUTHORIZED,
    error = "otp-expired",
    message = "The otp has expired",
    details = "The otp has expired"
)