package com.iplastudio.boilerplate.configs

import com.fasterxml.jackson.databind.ObjectMapper
import com.iplastudio.boilerplate.exceptions.ApiError
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Service

@Service
class CustomAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper
): AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        val responseBody = ApiError(
            error = "authentication-failed",
            message = "Authentication failed",
            details = "Failed to authenticate user, make sure you are providing some sort of authentication token"
        )
        response?.status = HttpStatus.UNAUTHORIZED.value()
        response?.contentType = "application/json"
        response?.writer?.write(objectMapper.writeValueAsString(responseBody))
    }

}