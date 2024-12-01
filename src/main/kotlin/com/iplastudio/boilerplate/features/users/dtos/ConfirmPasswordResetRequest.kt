package com.iplastudio.boilerplate.features.users.dtos

data class ConfirmPasswordResetRequest(
    val otp: String,
    val newPassword: String
)
