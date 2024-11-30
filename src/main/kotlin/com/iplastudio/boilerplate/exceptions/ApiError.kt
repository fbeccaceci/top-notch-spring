package com.iplastudio.boilerplate.exceptions

open class ApiError(
    val error: String,
    val message: String,
    val details: String
)
