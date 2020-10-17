package com.exentriquesolutions.covidukview.view

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@SpringBootApplication
@EnableR2dbcRepositories("com.exentriquesolutions.covidukview.db")
class RestViewer

private val log = KotlinLogging.logger { }

fun main() {
    try {
        runApplication<RestViewer>()
    } catch (exception: Exception) {
        log.error("Failed: $exception", exception)
    }
}
