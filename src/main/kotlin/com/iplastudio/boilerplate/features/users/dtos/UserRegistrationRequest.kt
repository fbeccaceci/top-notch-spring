package com.iplastudio.boilerplate.features.users.dtos

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.*

class UserRegistrationRequest(
    @field:NotBlank
    @field:Size(min = 3, message = "Must be at least 3 characters long")
    @field:Size(max = 32, message = "Must be at most 32 characters long")
    val username: String,

    @field:NotBlank
    @field:Email(message = "Must be a valid email")
    val email: String,

    @field:NotBlank
    @field:Size(min = 6, message = "Must be at least 6 characters long")
    val password: String,

    @JsonProperty("locale")
    localeTag: String?
) {

    @JsonIgnore
    val locale: Locale? = localeTag?.let { Locale.forLanguageTag(it) }

    @JsonProperty("locale")
    fun jsonGetLocale() = locale?.toLanguageTag()

}
