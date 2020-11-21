package com.exentriquesolutions.covidukview.db

import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

@Repository
interface RegionRepository : ReactiveCrudRepository<Region, Int> {
    fun getByNameAndType(name: String, type: AreaType): Mono<Region?>
}

@Repository
interface CasesRepository : ReactiveCrudRepository<Cases, Int> {
    fun getByDateAndRegionId(date: LocalDate, regionId: Int): Mono<Cases?>

    @Query("SELECT c.id, c.date,  AVG(c.count), OVER(ORDER BY c.date ROWS BETWEEN :days PRECEDING AND CURRENT ROW) AS avg_sales FROM cases c where c.region_id = :region_id")
    fun getRollingAverage(@Param("days") days: Int, @Param("region_id") regionId: Int): Flux<Cases>
}
