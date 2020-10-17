package com.exentriquesolutions.covidukview.loader

import com.exentriquesolutions.covidukview.db.Cases
import com.exentriquesolutions.covidukview.db.CasesRepository
import com.exentriquesolutions.covidukview.db.Region
import com.exentriquesolutions.covidukview.db.RegionRepository
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class CasesLoader(
        private val loader: ApiLoader,
        private val regionRepository: RegionRepository,
        private val casesRepository: CasesRepository
) {
    private val log = KotlinLogging.logger { }
    private val lastWeek = LocalDate.now().minusWeeks(1)

    suspend fun run(regionLimit: Int = Integer.MAX_VALUE) {
        loader.loadRegions().take(regionLimit).map {
            regionRepository.getByNameAndType(it.name, it.type).awaitFirstOrNull()
                    ?: save(it.toView())
        }.flatMap { region ->
            log.info { "Loading cases for $region" }
            loader.loadCases(region.toApi())
                    .map { cases -> cases.toView(region) }
        }.forEach {
            val cases = casesRepository.getByDateAndRegionId(it.date, it.regionId).awaitFirstOrNull()
            if (cases != null) {
                updateIfWithinLastWeek(cases.copy(count = it.count))
            } else {
                save(it)
            }
        }
    }

    private suspend fun updateIfWithinLastWeek(cases: Cases) {
        if (cases.date.isAfter(lastWeek)) {
            save(cases)
        }
    }

    private suspend fun save(region: Region): Region =
            regionRepository.save(region)
                    .awaitSingle()
                    .also {
                        log.info { "Saved $it" }
                    }

    private suspend fun save(cases: Cases): Cases =
            casesRepository.save(cases)
                    .awaitSingle()
                    .also {
                        log.info { "Saved $it" }
                    }

    private fun com.exentriquesolutions.covidukview.loader.Region.toView() =
            Region(name, type)

    private fun Region.toApi() =
            com.exentriquesolutions.covidukview.loader.Region(name, type)

    private fun com.exentriquesolutions.covidukview.loader.Cases.toView(region: Region) =
            Cases(date, count, region.id!!)
}
