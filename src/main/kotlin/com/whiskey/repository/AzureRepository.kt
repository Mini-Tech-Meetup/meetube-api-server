package com.whiskey.repository

import com.azure.storage.blob.BlobServiceClientBuilder
import com.whiskey.utils.AzureKey
import org.springframework.stereotype.Repository
import java.io.FileInputStream
import java.io.InputStream


@Repository
class AzureRepository {
    companion object{
        val containerName = AzureKey.StorageContainerName
        val connectString = AzureKey.StorageConnectionString
        const val storagePath = "./video/"
    }
    fun Upload(stream : FileInputStream,len:Long) {
        val container = BlobServiceClientBuilder()
            ?.connectionString(connectString)
            ?.buildClient()
            ?.getBlobContainerClient(containerName)

        val fileName = storagePath + java.util.UUID.randomUUID() + "mp4"
        val blobClient = container?.getBlobClient(fileName)

        blobClient?.upload(stream, len)
        //Todo. Client 에서 받는 파일포맷 확인
    }
}
