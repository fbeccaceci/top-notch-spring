package com.iplastudio.boilerplate.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app.self")
data class SelfProperties(
    val baseUrl: String
)