package com.exentriquesolutions.covidukview.view

import com.exentriquesolutions.covidukview.db.CasesRepository
import com.exentriquesolutions.covidukview.db.Region
import com.exentriquesolutions.covidukview.db.RegionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/api")
class CasesController(
        private val casesRepository: CasesRepository,
        private val regionRepository: RegionRepository
) {
    @GetMapping("/regions")
    suspend fun getAll(): Flow<Region> =
            regionRepository.findAll().asFlow()
}
