package com.iplastudio.boilerplate.configs

import com.iplastudio.boilerplate.features.security.jwt.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val customAuthenticationEntryPoint: CustomAuthenticationEntryPoint
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .httpBasic { it.disable() }
            .anonymous { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .csrf { it.disable() }
            .authorizeHttpRequests { requests ->
                requests.requestMatchers(
                    "/public/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/v3/api-docs/swagger-config",
                    "/users/sign-up",
                    "/users/activate-account",
                    "/users/login",
                    "/users/reset-password",
                    "/users/request-reset-password"
                ).permitAll()

                requests.anyRequest().authenticated()
            }

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        http.exceptionHandling {
            it.authenticationEntryPoint(customAuthenticationEntryPoint)
        }

        return http.build();
    }


    @Bean
    fun roleHierarchy(): RoleHierarchy = RoleHierarchyImpl.fromHierarchy(
        "ROLE_ADMIN > ROLE_USER",
    )

}