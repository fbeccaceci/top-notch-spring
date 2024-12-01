package com.iplastudio.boilerplate.features.emails.models

data class TemplatedEmail(
    val to: String,
    val subject: String,
    val templateName: String,
)
