package com.exentriquesolutions.covidukview

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.greaterThan
import com.natpryce.hamkrest.hasElement
import com.natpryce.hamkrest.hasSize
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LoaderTest {

    private val loader = Loader()

    @Test
    fun `should load regions from api`() {
        val regions = runBlocking { loader.loadRegions() }

        assertThat(regions, hasSize(greaterThan(500)))
        assertThat(regions, hasElement(Region("Southwark", AreaType.LowerTierLocaLAuthority)))
    }

    @Test
    fun `should load cases for a region from api`() {
        val cases = runBlocking { loader.loadCases(Region("Southwark", AreaType.LowerTierLocaLAuthority)) }

        assertThat(cases, hasSize(greaterThan(200)))
        assertThat(cases, hasElement(Cases(LocalDate.of(2020, 9, 6), 5)))
    }
}
