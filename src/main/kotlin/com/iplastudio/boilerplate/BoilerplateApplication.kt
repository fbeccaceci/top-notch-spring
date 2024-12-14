package com.iplastudio.boilerplate

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.web.config.EnableSpringDataWebSupport

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = ["com.iplastudio.boilerplate"])
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
class BoilerplateApplication

fun main(args: Array<String>) {
	runApplication<BoilerplateApplication>(*args)
}
