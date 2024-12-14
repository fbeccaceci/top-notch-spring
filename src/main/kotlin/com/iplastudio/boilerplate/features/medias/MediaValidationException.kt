package com.iplastudio.boilerplate.features.medias

import com.iplastudio.boilerplate.exceptions.AppException
import org.springframework.http.HttpStatus

class MediaValidationException(message: String) : AppException(
    responseStatus = HttpStatus.BAD_REQUEST,
    error = "media validation error",
    message = message,
    details = "A media validation failed due to: $message"
)