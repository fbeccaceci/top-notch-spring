package com.iplastudio.boilerplate

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BoilerplateApplication

fun main(args: Array<String>) {
	runApplication<BoilerplateApplication>(*args)
}
