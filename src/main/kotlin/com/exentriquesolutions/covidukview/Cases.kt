package com.exentriquesolutions.covidukview

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

class LocalDateDeserializer : JsonDeserializer<LocalDate>() {
    override fun deserialize(parser: JsonParser, context: DeserializationContext): LocalDate =
            LocalDate.parse(parser.text)
}

fun createObjectMapper(): ObjectMapper =
        ObjectMapper().registerModule(KotlinModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


data class Cases(
//        val day: LocalDate,
//        val count: Int,
//        val region: String
        @JsonDeserialize(using = LocalDateDeserializer::class) val date: LocalDate,
        val newCases: Int
)
