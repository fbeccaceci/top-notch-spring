package com.iplastudio.boilerplate.configs

import com.fasterxml.jackson.databind.Module
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.openapitools.jackson.nullable.JsonNullableModule
import org.springdoc.core.properties.SpringDocConfigProperties
import org.springdoc.core.providers.ObjectMapperProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class OpenApiConfig {

    private fun createAPIKeyScheme(): SecurityScheme {
        return SecurityScheme().type(SecurityScheme.Type.HTTP)
            .bearerFormat("JWT")
            .scheme("bearer")
    }

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI().addSecurityItem(
            SecurityRequirement().addList
                ("bearer-auth")
        )
            .components(Components().addSecuritySchemes("bearer-auth", createAPIKeyScheme()))
    }

    /**
     * Override default Spring Doc Object mapper provider and add the JsonNullableModule - otherwise the OpenAPI JSON
     * Spec interprets the JsonNullable wrong
     *
     * @param springDocConfigProperties given config properties in application yaml
     * @return provider for spring doc generation
     */
    @Bean
    fun springdocObjectMapperProvider(springDocConfigProperties: SpringDocConfigProperties?): ObjectMapperProvider {
        val objectMapperProvider = ObjectMapperProvider(springDocConfigProperties)
        objectMapperProvider.jsonMapper().registerModule(jsonNullableModule())
        return objectMapperProvider
    }


    /**
     * Register JsonNullableModule for openapi generation in springdoc (above) and for the default object mapper
     * used to serialize/deserialize JSON Payloads via web requests - otherwise the application is not able to map
     * JsonNullable<Type> fields
     *
     * @return json nullable module
    </Type> */
    @Bean
    fun jsonNullableModule(): Module {
        return JsonNullableModule()
    }

}