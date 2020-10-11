package com.exentriquesolutions.covidukview

import org.springframework.stereotype.Component

@Component
class Loader {
    private val apiClient = ApiClient()

    suspend fun loadRegions(): List<Region> =
            apiClient.lookup(
                    listOf(AreaType.LowerTierLocaLAuthority, AreaType.UpperTierLocalAuthority),
                    mapOf("name" to "areaName", "type" to "areaType")
            )

    suspend fun loadCases(region: Region): List<Cases> =
            apiClient.data(
                    region,
                    mapOf("date" to "date", "count" to "newCasesBySpecimenDate")
            )
}
