package com.iplastudio.boilerplate.exceptions

class ApiDataValidationError(
    error: String,
    message: String,
    details: String,
    val failedFields: List<FailedField>
): ApiError(error, message, details) {

    data class FailedField(val field: String, val reason: String?)

}
