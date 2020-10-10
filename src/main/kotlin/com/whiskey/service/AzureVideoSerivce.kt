package com.whiskey.service

import com.whiskey.client.MeetubeHttpClient
import com.whiskey.utils.AzureKey
import org.apache.http.entity.ContentType
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.apache.http.entity.mime.MultipartEntityBuilder
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.UUID

@Service
class AzureVideoSerivce {

    companion object {
        var accountId = AzureKey.videoIndexerUserId
        var location = "trial"; // replace with the account's location, or with “trial” if this is a trial account
        val apiKey = AzureKey.videoIndexerKey
        var apiUrl = "https://api.videoindexer.ai/$location/Accounts/$accountId"
        val header = mapOf(
            "x-ms-client-request-id" to "",
            "Ocp-Apim-Subscription-Key" to apiKey
        )
    }
    private val httpClient: MeetubeHttpClient = MeetubeHttpClient()

    fun fileUpload(file: MultipartFile) {

        getAccessToken()?.let {
            val parameter = mapOf(
                "name" to UUID.randomUUID().toString(),
                "accessToken" to it,
                "description" to "description",
                "privacy" to "public",
                "callbackUrl" to ""
            )
            val builder: MultipartEntityBuilder = MultipartEntityBuilder.create()

            builder.addBinaryBody("file", file.bytes, ContentType.APPLICATION_OCTET_STREAM, file.originalFilename)
            val body = builder.build()

            val a = httpClient.post("$apiUrl/Videos", parameter, header, body)
            val b = a.content.let { getStreamToString(it) }
            print(b)
        }
    }

    fun getAccessToken(): String? {
        val parameter = mapOf(
            "allowEdit" to "true"
        )
        val authUri = "https://api.videoindexer.ai/Auth/$location/Accounts/$accountId/AccessToken"
        val content = httpClient.get(authUri, parameter, header)?.entity?.content

        return content?.let(::getStreamToString)
    }

    fun getStreamToString(content: InputStream): String {
        val isr = InputStreamReader(content)
        val rd = BufferedReader(isr)

        return rd.readLine().trim().replace("\"", "")
    }
}
