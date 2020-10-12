package com.exentriquesolutions.covidukview.view

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.LocalDate

@Repository
interface RegionRepository : ReactiveCrudRepository<Region, Int> {
    fun getByNameAndType(name: String, type: AreaType): Mono<Region?>
}

@Repository
interface CasesRepository : ReactiveCrudRepository<Cases, Int> {
    fun getByDateAndRegionId(date: LocalDate, regionId: Int): Mono<Cases?>
}
