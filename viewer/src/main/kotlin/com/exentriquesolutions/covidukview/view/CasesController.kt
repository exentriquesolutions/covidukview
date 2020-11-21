package com.exentriquesolutions.covidukview.view

import com.exentriquesolutions.covidukview.db.CasesRepository
import com.exentriquesolutions.covidukview.db.Region
import com.exentriquesolutions.covidukview.db.RegionRepository
import com.exentriquesolutions.covidukview.db.RollingAverage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController("/api")
class CasesController(
        private val casesRepository: CasesRepository,
        private val regionRepository: RegionRepository,
        private val databaseClient: DatabaseClient
) {
    @GetMapping("/regions")
    suspend fun getAll(): Flow<Region> =
            regionRepository.findAll().asFlow()

    // TODO this is wrong - we want a rolling "new cases per week per 100,000"
    @GetMapping("/region/{region_id}")
    suspend fun getWeeklyRollingAverage(@PathVariable("region_id") regionId: Int): Flow<RollingAverage> =
            databaseClient.execute("SELECT c.date,  AVG(c.count) OVER(ORDER BY c.date ROWS BETWEEN 6 PRECEDING AND CURRENT ROW) AS avg_cases FROM cases c where c.region_id = :region_id")
                    .bind("region_id", regionId)
                    .`as`(RollingAverage::class.java)
                    .fetch()
                    .all()
                    .asFlow()
}
