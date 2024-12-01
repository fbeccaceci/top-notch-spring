package com.iplastudio.boilerplate

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = ["com.iplastudio.boilerplate"])
class BoilerplateApplication

fun main(args: Array<String>) {
	runApplication<BoilerplateApplication>(*args)
}
