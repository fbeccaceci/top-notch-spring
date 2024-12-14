package com.iplastudio.boilerplate.configs

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.SimpleType
import io.swagger.v3.core.converter.AnnotatedType
import io.swagger.v3.core.converter.ModelConverter
import io.swagger.v3.core.converter.ModelConverterContext
import io.swagger.v3.core.jackson.ModelResolver
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.openapitools.jackson.nullable.JsonNullableModule
import org.springdoc.core.properties.SpringDocConfigProperties
import org.springdoc.core.providers.ObjectMapperProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component


@Configuration
class OpenApiConfig {

    companion object {
        init {
            ModelResolver.enumsAsRef = true
        }
    }

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
    fun springdocObjectMapperProvider(
        springDocConfigProperties: SpringDocConfigProperties?,
        jsonNullableModule: JsonNullableModule): ObjectMapperProvider {
        val objectMapperProvider = ObjectMapperProvider(springDocConfigProperties)

        objectMapperProvider.jsonMapper().registerModule(jsonNullableModule)

        return objectMapperProvider
    }

}


/**
 * Needed to correctly name enums defined inside other classes
 * eg: a Status enum inside the User and Client classes, without this would be named Status and the wou would collide
 * with this fix they will be named User$Status and Client$Status
 */
@Component
class EnumsScopeModelResolver(objectMapper: ObjectMapper): ModelResolver(objectMapper) {

    override fun resolve(
        annotatedType: AnnotatedType,
        context: ModelConverterContext,
        next: MutableIterator<ModelConverter>
    ): Schema<*> {
        val schema = super.resolve(annotatedType, context, next)

        val objectType = annotatedType.type
        if (objectType is SimpleType && objectType.rawClass.isEnum) {
            val name = "${objectType.rawClass.enclosingClass.simpleName}${objectType.rawClass.simpleName}"

            val model = context.resolve(annotatedType)
            context.defineModel(name, model, annotatedType, null)

            schema.`$ref` = name
        }

        return schema

    }

}