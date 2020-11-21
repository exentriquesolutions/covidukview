package com.exentriquesolutions.covidukview.db

import org.springframework.data.annotation.Id
import java.time.LocalDate

data class Region(
        val name: String,
        val type: AreaType,
        @Id
        val id: Int? = null,
)

data class Cases(
        val date: LocalDate,
        val count: Int,
        val regionId: Int,
        @Id
        val id: Long? = null,
)

enum class AreaType(val apiCode: String) {
    UpperTierLocalAuthority("utla"),
    LowerTierLocaLAuthority("ltla")
}

data class RollingAverage(
        val date: LocalDate,
        val count: Float
)
