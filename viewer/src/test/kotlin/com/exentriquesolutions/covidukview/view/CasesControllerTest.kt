package com.exentriquesolutions.covidukview.view

import com.exentriquesolutions.covidukview.db.Region
import com.exentriquesolutions.covidukview.db.RegionRepository
import com.flextrade.kfixture.KFixture
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CasesControllerTest {
    private val fixture = KFixture()

    @Autowired
    private lateinit var regionRepository: RegionRepository

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    private lateinit var region: Region

    @BeforeEach
    fun setup() {
        region = regionRepository.save(fixture<Region>().copy(id = null)).block()!!
    }
    @Test
    fun `should return regions`() {
        val regions = restTemplate.getForEntity<Array<Region>>("/regions").body!!.toList()

        assertThat(regions, hasSize(equalTo(1)))
        assertThat(regions[0], equalTo(region))
    }
}
