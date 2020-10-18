package com.exentriquesolutions.covidukview.view

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class HomeControllerTest {
    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun `should return home page`() {
        val homepage = restTemplate.getForEntity<String>("/").body

        assertThat(homepage, containsSubstring("COVID UK Statistics"))
    }
}
