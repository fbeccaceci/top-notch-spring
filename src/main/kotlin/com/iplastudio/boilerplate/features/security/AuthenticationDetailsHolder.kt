package com.iplastudio.boilerplate.features.security

import com.iplastudio.boilerplate.features.security.exceptions.InvalidAuthenticationDetailsException
import org.springframework.security.core.context.SecurityContextHolder

object AuthenticationDetailsHolder {

    val details: AuthenticationDetails?
        get() {
            val authentication = SecurityContextHolder.getContext().authentication ?: return null

            val principal = authentication.principal

            if (principal !is AuthenticationDetails) {
                throw InvalidAuthenticationDetailsException(
                    details = "Principal is not an instance of AuthenticationDetails"
                )
            }

            return principal
        }

}