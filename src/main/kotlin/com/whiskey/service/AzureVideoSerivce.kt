package com.whiskey.service

import com.whiskey.client.MeetubeHttpClient
import com.whiskey.entity.Video
import com.whiskey.repository.AzureRepository
import com.whiskey.utils.AzureKey
import org.apache.http.entity.ContentType
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.*
import java.util.*


@Service
class AzureVideoSerivce (private  val azureRepository: AzureRepository){

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

    fun getVideoIndexerInfomation (oldVideo:Video) : Video {
        return  oldVideo
    }

    fun fileUpload(file: MultipartFile) : Video {
        val tempFile = File(file.originalFilename)
        tempFile.createNewFile()
        val fos = FileOutputStream(tempFile)
        fos.write(file.bytes)
        fos.close()
        val uuid = UUID.randomUUID().toString()
        val azurePath = azureRepository.upload(tempFile.absolutePath,uuid)


        val videoId = getAccessToken()?.let {
            val parameterForUpload = mapOf(
                "name" to uuid,
                "accessToken" to it,
                "description" to "description",
                "privacy" to "public",
                "videoUrl" to (azurePath ?: ""),
                "externalId" to uuid
            )

            httpClient.post("$apiUrl/Videos", parameterForUpload, header)?.content?.let { println(getStreamToString(it)) }
            print("Upload Video")
            val parameterForId = mapOf<String,String>(
                "externalId" to uuid,
                "accessToken" to it
            )
            val content = httpClient.get("${apiUrl}/Videos/GetIdByExternalId",parameterForId, header)?.entity?.content
            content?.let (::getStreamToString)
        }

        tempFile.delete()

        return Video(
            videoId ?: uuid,
            file.originalFilename ?: "Untitled",
            null,
            null,
            null,
            azurePath,
            null
        )
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
