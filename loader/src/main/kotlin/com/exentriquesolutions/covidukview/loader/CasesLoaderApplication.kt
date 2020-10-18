package com.exentriquesolutions.covidukview.loader

import kotlinx.coroutines.*
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import kotlin.system.exitProcess

@SpringBootApplication
@EnableR2dbcRepositories("com.exentriquesolutions.covidukview.db")
class CasesLoaderApplication

private val log = KotlinLogging.logger { }

fun main(vararg args: String) {
    try {
        val casesLoader = runApplication<CasesLoaderApplication>(*args).getBean(CasesLoader::class.java)
        runBlocking {
            val runs = async(Dispatchers.Default) {
                val parallelism = 4
                repeat(parallelism) {
                    launch { casesLoader.run() }
                }
            }
            runs.join()
        }
    } catch (exception: Exception) {
        log.error("Failed: ${exception.message}", exception)
        exitProcess(1)
    }
}
