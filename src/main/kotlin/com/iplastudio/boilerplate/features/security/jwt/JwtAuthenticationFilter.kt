package com.iplastudio.boilerplate.features.security.jwt

import com.iplastudio.boilerplate.features.security.AuthenticationDetails
import jakarta.servlet.FilterChain
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val handlerExceptionResolver: HandlerExceptionResolver
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getParameter("auth")
            ?: extractTokenFromHeader(request)
            ?: getCookieValue(request, "auth")

        if (token == null || SecurityContextHolder.getContext().authentication != null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            val details = AuthenticationDetails(
                username = jwtService.extractUsername(token),
                userId = jwtService.extractUserId(token)
            )

            SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
                details,
                null,
                jwtService.extractGrantedAuthorities(token)
            )

            filterChain.doFilter(request, response);
        } catch (e: RuntimeException) {
            handlerExceptionResolver.resolveException(request, response, null, e)
        }
    }

    private fun extractTokenFromHeader(request: HttpServletRequest): String? {
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null

        return authHeader.substring("Bearer ".length)
    }

    fun getCookieValue(request: HttpServletRequest, cookieName: String?): String? {
        val cookies: Array<Cookie>? = request.cookies
        if (cookies != null) {
            for (cookie in cookies) {
                if (cookie.name.equals(cookieName)) {
                    return cookie.value
                }
            }
        }
        return null // Cookie not found
    }

}