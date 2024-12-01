package com.iplastudio.boilerplate.configs

import com.fasterxml.jackson.annotation.JsonInclude
import org.openapitools.jackson.nullable.JsonNullableModule
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfig {

    @Bean
    fun jsonCustomizer(): Jackson2ObjectMapperBuilderCustomizer = Jackson2ObjectMapperBuilderCustomizer { builder ->
        builder.serializationInclusion(JsonInclude.Include.NON_NULL)
    }

    /**
     * Register JsonNullableModule for openapi generation in springdoc (above) and for the default object mapper
     * used to serialize/deserialize JSON Payloads via web requests - otherwise the application is not able to map
     * JsonNullable<Type> fields
     *
     * @return json nullable module
    </Type> */
    @Bean
    fun jsonNullableModule(): JsonNullableModule {
        return JsonNullableModule()
    }

}