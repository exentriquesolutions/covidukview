package com.exentriquesolutions.covidukview.loader

import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import kotlin.system.exitProcess

@SpringBootApplication
@ComponentScan(
        "com.exentriquesolutions.covidukview.api",
        "com.exentriquesolutions.covidukview.loader"
)
@EnableR2dbcRepositories("com.exentriquesolutions.covidukview.db")
class CasesLoaderApplication

private val log = KotlinLogging.logger { }

fun main(vararg args: String) {
    try {
        runBlocking {
            runApplication<CasesLoaderApplication>(*args).getBean(CasesLoader::class.java).run()
        }
    } catch (exception: Exception) {
        log.error("Failed: ${exception.message}", exception)
        exitProcess(1)
    }
}
