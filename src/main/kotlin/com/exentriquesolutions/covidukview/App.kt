package com.exentriquesolutions.covidukview

import com.exentriquesolutions.covidukview.loader.CasesLoader
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class App

private val log = KotlinLogging.logger { }

fun main(vararg args: String) {
    try {
        runBlocking {
            runApplication<App>(*args).getBean(CasesLoader::class.java).run()
        }
    } catch (exception: Exception) {
        log.error("Failed: ${exception.message}", exception)
    }
}
