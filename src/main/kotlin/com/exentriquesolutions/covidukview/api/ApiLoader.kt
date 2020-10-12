package com.exentriquesolutions.covidukview.api

import com.exentriquesolutions.covidukview.view.AreaType
import org.springframework.stereotype.Component

@Component
class ApiLoader {
    private val apiClient = ApiClient()

    suspend fun loadRegions(): List<Region> =
            apiClient.lookup(
                    listOf(AreaType.LowerTierLocaLAuthority, AreaType.UpperTierLocalAuthority),
                    mapOf("name" to "areaName", "type" to "areaType")
            )

    suspend fun loadCases(region: Region): List<Cases> =
            apiClient.data(
                    region.type,
                    region.name,
                    mapOf("date" to "date", "count" to "newCasesBySpecimenDate")
            )
}
