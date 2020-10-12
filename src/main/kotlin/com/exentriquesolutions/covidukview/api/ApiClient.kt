package com.exentriquesolutions.covidukview.api

import com.exentriquesolutions.covidukview.view.AreaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.future.await
import org.apache.commons.io.IOUtils
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

class ApiClient(private val objectMapper: ObjectMapper = createObjectMapper()) {
    suspend fun lookup(areaTypes: List<AreaType>, structureMap: Map<String, String>): List<Region> =
            apiCall(ApiEndPoint.LookUpEndPoint(areaTypes), structureMap)

    suspend fun data(areaType: AreaType, regionName: String, structureMap: Map<String, String>): List<Cases> =
            apiCall(ApiEndPoint.DataApiEndPoint(areaType, regionName), structureMap)

    private suspend inline fun <reified T> apiCall(endPoint: ApiEndPoint<*>, structureMap: Map<String, String>): List<T> {
        val structure = createStructure(structureMap)
        val request = createRequest(endPoint, structure)
        val client = createClient()
        val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
                .thenApply {
                    if (it.statusCode() != 200) {
                        error("Response failed: $it")
                    }
                    it.body()
                }
                .thenApply(Companion::gunzip)
                .thenApply {
                    val results = objectMapper.readValue<Map<String, Any>>(it)
                    @Suppress("UNCHECKED_CAST") val data = results["data"] as List<Map<Any, Any>>
                    data.map { itemMap ->
                        objectMapper.convertValue(itemMap, T::class.java)
                    }
                }
        return response.await()
    }

    private fun createStructure(structure: Map<String, String>): String =
            objectMapper.writeValueAsString(structure)

    companion object {
        private val TIMEOUT: Duration = Duration.ofSeconds(10)

        private sealed class ApiEndPoint<T>(val endPoint: String) {
            abstract fun buildFilter(): String

            class LookUpEndPoint(private val areaTypes: List<AreaType>) : ApiEndPoint<List<AreaType>>("lookup") {
                override fun buildFilter(): String =
                        areaTypes.joinToString(separator = "|") { "areaType=${it.apiCode}" }.encodeUrl()
            }

            class DataApiEndPoint(val areaType: AreaType, val regionName: String) : ApiEndPoint<Region>("data") {
                override fun buildFilter(): String =
                        "areaType=${areaType.apiCode};areaName=$regionName"
            }
        }

        private fun buildUri(apiEndPoint: ApiEndPoint<*>, structure: String) =
                URI.create("https://api.coronavirus.data.gov.uk/v1/${apiEndPoint.endPoint}?filters=${apiEndPoint.buildFilter().encodeUrl()}&structure=${structure.encodeUrl()}")

        private fun String.encodeUrl() = URLEncoder.encode(this, StandardCharsets.UTF_8)

        fun gunzip(compressedStream: InputStream): String =
                GZIPInputStream(compressedStream).use { gzipStream ->
                    StringWriter().use {
                        IOUtils.copy(gzipStream, it, StandardCharsets.UTF_8)
                        it.toString()
                    }
                }

        private fun createClient(): HttpClient {
            return HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .connectTimeout(TIMEOUT)
                    .build()
        }

        private fun createRequest(endPoint: ApiEndPoint<*>, structure: String): HttpRequest =
                HttpRequest.newBuilder()
                        .uri(buildUri(endPoint, structure))
                        .timeout(TIMEOUT)
                        .header("Accepts", "application/json")
                        .build()
    }

}
