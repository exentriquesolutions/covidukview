package com.exentriquesolutions.covidukview.loader

import com.exentriquesolutions.covidukview.db.AreaType
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDate

class LocalDateDeserializer : JsonDeserializer<LocalDate>() {
    override fun deserialize(parser: JsonParser, context: DeserializationContext): LocalDate =
            LocalDate.parse(parser.text)
}

class AreaTypeDeserializer : JsonDeserializer<AreaType>() {
    override fun deserialize(parser: JsonParser, context: DeserializationContext): AreaType =
            when (parser.text) {
                AreaType.UpperTierLocalAuthority.apiCode -> AreaType.UpperTierLocalAuthority
                AreaType.LowerTierLocaLAuthority.apiCode -> AreaType.LowerTierLocaLAuthority
                else -> error("Unknown area type: ${parser.text}")
            }
}

fun createObjectMapper(): ObjectMapper =
        ObjectMapper().registerModule(KotlinModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

data class Region(
        val name: String,

        @JsonDeserialize(using = AreaTypeDeserializer::class)
        val type: AreaType,
)

data class Cases(
        @JsonDeserialize(using = LocalDateDeserializer::class)
        val date: LocalDate,
        val count: Int,
)
