package io.openfuture.api.client

import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.methods.*
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Component
class DefaultHttpClientWrapper : HttpClientWrapper {

    private lateinit var httpClient: CloseableHttpClient

    val log: Logger = LoggerFactory.getLogger(DefaultHttpClientWrapper::class.java)


    init {
        val provider = BasicCredentialsProvider().apply {
            setCredentials(AuthScope.ANY, UsernamePasswordCredentials("user", "password"))
        }
        httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build()
    }

    override fun get(path: String, headers: Map<String, String>, params: Map<String, String>): HttpResponse {
        val url = buildRequestUrl(path, params)
        val request = HttpGet(url)
        headers.entries.forEach { request.addHeader(it.key, it.value) }

        log.info("Send GET request: $url")
        return execute(request)
    }

    override fun post(path: String, headers: Map<String, String>, body: Any, params: Map<String, String>): HttpResponse {
        val url = buildRequestUrl(path, params)
        val jsonRequest = BodyConverter.serialize(body)
        val request = HttpPost(url)
        headers.entries.forEach { request.setHeader(it.key, it.value) }
        request.entity = StringEntity(jsonRequest, ContentType.APPLICATION_JSON)

        log.info("Send POST request: $url: $body")
        return execute(request)
    }

    override fun put(path: String, headers: Map<String, String>, body: Any, params: Map<String, String>): HttpResponse {
        val url = buildRequestUrl(path, params)
        val jsonRequest = BodyConverter.serialize(body)
        val request = HttpPut(url)
        headers.entries.forEach { request.addHeader(it.key, it.value) }
        params.entries.forEach { request.setHeader(it.key, it.value) }
        request.entity = StringEntity(jsonRequest, ContentType.APPLICATION_JSON)

        log.info("Send PUT request: $url: $body")
        return execute(request)
    }

    override fun delete(path: String, headers: Map<String, String>, params: Map<String, String>): HttpResponse {
        val url = buildRequestUrl(path, params)
        val request = HttpDelete(url)
        headers.entries.forEach { request.setHeader(it.key, it.value) }

        log.info("Send DELETE request: $url")
        return execute(request)
    }

    private fun buildRequestUrl(path: String, params: Map<String, String>): URI {
        val builder = UriComponentsBuilder.fromHttpUrl(path)
        params.forEach { builder.queryParam(it.key, it.value) }
        return builder.build().toUri()
    }

    private fun execute(request: HttpRequestBase): HttpResponse {
        httpClient.execute(request).use { response ->
            val result = response.entity?.let { EntityUtils.toString(response.entity, Charsets.UTF_8) } ?: ""
            EntityUtils.consume(response.entity)
            val httpResponse = HttpResponse(response.allHeaders.associate { it.name to it.value }, result)

            log.info("Received response: ${httpResponse.entity}")
            return httpResponse
        }
    }

}
