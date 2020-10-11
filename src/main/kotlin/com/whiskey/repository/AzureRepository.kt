package com.whiskey.repository

import com.azure.storage.blob.BlobServiceClientBuilder
import com.whiskey.utils.AzureKey
import org.springframework.stereotype.Repository
import java.io.InputStream

@Repository
class AzureRepository {
    companion object {
        val containerName = AzureKey.StorageContainerName
        val connectString = AzureKey.StorageConnectionString
        val storagePath = "https://meetupmedia.blob.core.windows.net/$containerName/"
    }

    fun upload(stream: InputStream, len: Long, uuid: String) {
        print(connectString)

        val blobServiceClient = BlobServiceClientBuilder().connectionString(connectString).buildClient()
        print(connectString)

        val containerClient = blobServiceClient.getBlobContainerClient(containerName)

        val fileName = "$uuid.mp4"

        val blobClient = containerClient.getBlobClient(fileName)

        blobClient.upload(stream, len)

        val aaa = "asdasdf"
        print(aaa)
    }

    fun upload(stream: String, file: String): String? {
        print(connectString)

        val blobServiceClient = BlobServiceClientBuilder().connectionString(connectString).buildClient()
        print(connectString)

        val containerClient = blobServiceClient.getBlobContainerClient(containerName)

        val fileName = file

        val blobClient = containerClient.getBlobClient(fileName)

        blobClient.uploadFromFile(stream,true)
        return blobClient.blobUrl
    }
}
