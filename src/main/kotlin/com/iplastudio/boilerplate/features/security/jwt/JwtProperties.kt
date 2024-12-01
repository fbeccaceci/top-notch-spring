package com.iplastudio.boilerplate.features.security.jwt

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app.security.jwt")
data class JwtProperties(
    val secret: String,
    val expirationTime: Long
)
