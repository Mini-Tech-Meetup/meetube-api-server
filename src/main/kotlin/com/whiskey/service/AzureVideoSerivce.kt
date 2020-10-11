package com.whiskey.service

import com.whiskey.client.MeetubeHttpClient
import com.whiskey.entity.Video
import com.whiskey.repository.AzureRepository
import com.whiskey.utils.AzureKey
import org.apache.http.util.EntityUtils
import org.json.JSONObject
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.*
import java.util.*

@Service
class AzureVideoSerivce(private val azureRepository: AzureRepository) {

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

    fun getVideoIndexerInfomation(oldVideo: Video): Video {
        val id = oldVideo.id

        return getAccessToken()?.let {
            // 옛날 비디오 아이디로 메타데이터 가져오기
            val parameterForIndex = mapOf(
                "accessToken" to it
            )

            val content = httpClient.get("${apiUrl}/Videos/${id}/Index", parameterForIndex, header)?.entity

            if (content != null) {
                val retSrc = EntityUtils.toString(content)
                val videoIndex = JSONObject(retSrc)
                val state = videoIndex.getString("state")
                // 아직 처리되지 않았다면
                if(state != "Processed"){
                    return oldVideo
                }
                // 처리되었다면
                else if(state == "Processed"){
                    val insight = videoIndex.getJSONObject("summarizedInsights")
                    val keywordsJson = insight.getJSONArray("keywords")
                    val keywords = keywordsJson.map { (it as JSONObject).getString("name") }
                    // thumbnail ID 가져옴
                    val thumbnailId = insight.getString("thumbnailId")
                    //tumbnail ID 로 썸네일 스트림가져옴
                    val thumbnailPath = getAccessToken()?.let {
                        val parameterForThumbnail = mapOf<String,String>(
                            "thumbnailId" to thumbnailId,
                            "format" to "Base64",
                            "accessToken" to it
                        )
                        val thumbnail = httpClient.get("${apiUrl}/Videos/${id}/Thumbnails", parameterForThumbnail, header)?.entity?.content
                        val tempFile = File("$thumbnailId.jpg")
                        tempFile.createNewFile()
                        val fos = FileOutputStream(tempFile)
                        val buffer = ByteArray(thumbnail?.available() as Int)
                        thumbnail?.read(buffer)
                        fos.write(buffer)
                        fos.close()
                        val azurePath = azureRepository.upload(tempFile.absolutePath, "$thumbnailId.jpg")
                        tempFile.delete()
                        azurePath
                    }





                    //Get Video Captions 로 txt 로 설정해서 스크립트 다 가져옴
                    val script = getAccessToken()?.let {
                        val parameterForCaption = mapOf<String, String>(
                            "format" to "Txt",
                            "accessToken" to it
                        )
                        val entity = httpClient.get("${apiUrl}/Videos/${id}/Captions", parameterForCaption, header)?.entity
                        val script = EntityUtils.toString(entity)
                        print(script)
                        script
                    }

                    return oldVideo.copy(keywords = keywords.toTypedArray(),
                        caption = script,
                        thumbnailUrl = thumbnailPath,
                        description = "Incomplete Sorry :("
                        )

                }
            }

            oldVideo
        } ?: oldVideo
    }

    fun fileUpload(file: MultipartFile): Video {
        val tempFile = File(file.originalFilename)
        tempFile.createNewFile()
        val fos = FileOutputStream(tempFile)
        fos.write(file.bytes)
        fos.close()
        val uuid = UUID.randomUUID().toString()
        val azurePath = azureRepository.upload(tempFile.absolutePath, "$uuid.mp4")

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
            val parameterForId = mapOf(
                "externalId" to uuid,
                "accessToken" to it
            )
            val content = httpClient.get("$apiUrl/Videos/GetIdByExternalId", parameterForId, header)?.entity?.content
            content?.let(::getStreamToString)
        }

        tempFile.delete()

        return Video(
            videoId ?: uuid,
            file.originalFilename ?: "Untitled",
            arrayOf(),
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
