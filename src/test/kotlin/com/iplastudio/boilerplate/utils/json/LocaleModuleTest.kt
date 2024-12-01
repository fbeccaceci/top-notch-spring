package com.iplastudio.boilerplate.utils.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class LocaleModuleTest {

    private val objectMapper = ObjectMapper()
        .registerKotlinModule()
        .registerModule(LocaleModule())

    @Test
    fun `serialize Locale to language tag`() {
        val locale = Locale.forLanguageTag("en-US")
        val jsonString = objectMapper.writeValueAsString(locale)

        assertEquals(jsonString, "\"en-US\"")
    }

    @Test
    fun `deserialize language tag to Locale`() {
        val jsonString = "\"en-US\""
        val locale = objectMapper.readValue(jsonString, Locale::class.java)

        assertEquals(locale, Locale.forLanguageTag("en-US"))
    }

}