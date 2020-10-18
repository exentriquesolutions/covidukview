package com.exentriquesolutions.covidukview.view

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.greaterThan
import com.natpryce.hamkrest.hasSize
import mu.KotlinLogging
import org.apache.commons.io.IOUtils
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.Select
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.PosixFilePermission.*
import java.util.zip.ZipInputStream

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IntegrationTest {
    private lateinit var driver: ChromeDriver

    @LocalServerPort
    private var port: Int = 0

    @BeforeAll
    internal fun setupChromeDriverAndWebpack() {
        if (!Files.exists(CHROME_DRIVER_PATH)) {
            val request = HttpRequest.newBuilder().GET().uri(CHROME_DRIVER_URL).build()
            val response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofInputStream())
            saveZipFile(response.body())
            LOG.info { "Saved $CHROME_DRIVER_PATH" }
            val withExecutePermissions = Files.getPosixFilePermissions(CHROME_DRIVER_PATH) + setOf(OTHERS_EXECUTE, OWNER_EXECUTE, GROUP_EXECUTE)
            Files.setPosixFilePermissions(CHROME_DRIVER_PATH, withExecutePermissions)
        }
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH.toString())
        driver = ChromeDriver()
    }

    @AfterAll
    internal fun cleanup() {
        driver.close()
    }

    @Test
    fun `should have a list of regions`() {
        driver.navigate().to("http://localhost:$port/")
        eventually {
            val findElementById = Select(driver.findElementById("regions"))
            assertThat(findElementById.options, hasSize(greaterThan(0)))
        }
    }

    companion object {
        private val LOG = KotlinLogging.logger { }
        private val CHROME_DRIVER_URL = URI("https://chromedriver.storage.googleapis.com/86.0.4240.22/chromedriver_linux64.zip")
        private val CHROME_DRIVER_PATH = Paths.get("chromedriver").toAbsolutePath()


        private fun saveZipFile(compressedStream: InputStream) =
                ZipInputStream(compressedStream).use { zipStream ->
                    zipStream.nextEntry
                    FileOutputStream(CHROME_DRIVER_PATH.toFile()).use { fileStream ->
                        IOUtils.copy(zipStream, fileStream)
                    }
                }

        private fun eventually(task: () -> Unit) {
            while(true) {
                val timeout = System.currentTimeMillis() + 1000
                try {
                    task()
                    return
                } catch (exception: AssertionError) {
                    if (System.currentTimeMillis() > timeout) {
                        throw exception
                    }
                }
            }
        }
    }
}
