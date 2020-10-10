package com.whiskey.repository

import com.azure.storage.blob.*
import com.whiskey.utils.AzureKey
import org.apache.http.impl.client.HttpClientBuilder
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
    fun Upload(stream : InputStream, len:Long, uuid:String) {
        print(connectString)

        val blobServiceClient = BlobServiceClientBuilder().sasToken().connectionString(connectString).buildClient()
        print(connectString)

        val containerClient = blobServiceClient.createBlobContainer(containerName)

        val fileName = "$uuid.mp4"

        val blobClient = containerClient.getBlobClient(fileName)


        blobClient.upload(stream,len);

    }
}
