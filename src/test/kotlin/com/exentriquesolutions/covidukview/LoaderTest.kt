package com.exentriquesolutions.covidukview

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.greaterThan
import com.natpryce.hamkrest.hasSize
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

val isNotEmpty = hasSize(greaterThan(0))

class LoaderTest {

    private val loader = Loader(createObjectMapper())

    @Test
    fun `should load data from api`() {
        val cases = runBlocking { loader.load() }

        assertThat(cases, isNotEmpty)
    }
}
