package com.whiskey.client

import org.apache.http.HttpEntity
import org.apache.http.HttpMessage
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.utils.URIBuilder
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.springframework.stereotype.Component
import java.net.URI

@Component
class MeetubeHttpClient {
    fun post(
        uriString: String,
        params: Map<String, String>,
        headers: Map<String, String>,
        body: HttpEntity
    ): HttpEntity {
        val httpclient = HttpClients.createDefault()
        val uri = buildUri(uriString, params)
        val request = HttpPost(uri)

        request.setHeaders(headers)
        request.entity = body

        val response = httpclient.execute(request)

        return response.entity
    }

    fun get(
        uriString: String,
        params: Map<String, String>,
        headers: Map<String, String>
    ): CloseableHttpResponse? {
        val httpclient = HttpClients.createDefault()
        val uri = buildUri(uriString, params)
        val request = HttpGet(uri)

        request.setHeaders(headers)

        return httpclient.execute(request)
    }

    /**
     * @param uriString : ex) "https://api.videoindexer.ai/{location}/Accounts/{accountId}/Videos?name={name}"
     * @param params: ex) mapOf("privacy" to "Private", "priority" to "Priority")
     */
    private fun buildUri(uriString: String, params: Map<String, String>): URI {
        val builder = URIBuilder(uriString)
        params.forEach { (param, value) -> builder.setParameter(param, value) }
        return builder.build()
    }

    private fun getEntity(bodyString: String) = StringEntity(bodyString)

    /**
     * @param headers: ex) mapOf("Content-Type" to "multipart/form-data")
     */
    private fun HttpMessage.setHeaders(headers: Map<String, String>) = headers
        .forEach { (param, value) -> this.setHeader(param, value) }
}
