package com.iplastudio.boilerplate.features.users.dtos

data class UserAuthenticationResponse(
    val token: String,
    val refreshToken: String
)