package com.exentriquesolutions.covidukview

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.future.await
import org.apache.commons.io.IOUtils
import org.springframework.stereotype.Component
import java.io.InputStream
import java.io.StringWriter
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.util.zip.GZIPInputStream

@Component
class Loader(private val objectMapper: ObjectMapper) {
    suspend fun load(): List<Cases> {
        val structure = createStructure()
        val request = createRequest(structure)
        val client = createClient()
        val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
                .thenApply(HttpResponse<InputStream>::body)
                .thenApply(Loader::gunzip)
                .thenApply {
                    objectMapper.readValue(it, Response::class.java)
                }.thenApply(Response::data)
        return response.await()
    }

    private fun createClient(): HttpClient {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(TIMEOUT)
                .build()
    }

    private fun createRequest(structure: String) =
            HttpRequest.newBuilder()
                    .uri(buildUri(structure))
                    .timeout(TIMEOUT)
                    .header("Accepts", "application/json")
                    .build()

    private fun createStructure(): String {
        return objectMapper.writeValueAsString(
                mapOf(
                        "date" to "date",
                        "newCases" to "newCasesByPublishDate"
                )
        )
    }

    private data class Response(
            val data: List<Cases>,
    )

    companion object {
        private val TIMEOUT: Duration = Duration.ofSeconds(10)
        private fun buildUri(structure: String) =
                URI.create("https://api.coronavirus.data.gov.uk/v1/data?filters=areaType=nation;areaName=england&structure=${structure.encodeUrl()}")

        private fun String.encodeUrl() = URLEncoder.encode(this, StandardCharsets.UTF_8)

        private fun gunzip(compressedStream: InputStream): String =
                GZIPInputStream(compressedStream).use { gzipStream ->
                    val writer = StringWriter()
                    IOUtils.copy(gzipStream, writer, StandardCharsets.UTF_8)
                    writer.toString()
                }
    }
}
