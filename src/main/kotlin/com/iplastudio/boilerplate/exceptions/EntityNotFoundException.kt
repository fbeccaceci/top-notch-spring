package com.iplastudio.boilerplate.exceptions

import org.springframework.http.HttpStatus
import kotlin.reflect.KClass

class EntityNotFoundException(entityClass: KClass<*>): AppException(
    responseStatus = HttpStatus.NOT_FOUND,
    error = "entity-not-found",
    message = "${entityClass.simpleName} was not found",
    details = "${entityClass.simpleName} was not found"
)