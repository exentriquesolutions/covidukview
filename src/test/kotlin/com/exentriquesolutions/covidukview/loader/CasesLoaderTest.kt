package com.exentriquesolutions.covidukview.loader

import com.exentriquesolutions.covidukview.db.CasesRepository
import com.exentriquesolutions.covidukview.db.RegionRepository
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.greaterThan
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class CasesLoaderTest {
    @Autowired
    private lateinit var casesLoader: CasesLoader

    @Autowired
    private lateinit var regionRepository: RegionRepository

    @Autowired
    private lateinit var casesRepository: CasesRepository

    @Test
    fun `should load cases into database`() {
        runBlocking { casesLoader.run(regionLimit = 1) }

        assertThat(regionRepository.count().block()!!, equalTo(1))
        assertThat(casesRepository.count().block()!!, greaterThan(5))
    }
}
