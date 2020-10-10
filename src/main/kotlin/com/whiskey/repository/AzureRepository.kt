package com.whiskey.repository

import com.whiskey.utils.AzureKey
import org.springframework.stereotype.Repository
import java.io.FileInputStream

@Repository
class AzureRepository {
    companion object {
        val containerName = AzureKey.StorageContainerName
        val connectString = AzureKey.StorageConnectionString
        const val storagePath = "./video/"
    }

    fun Upload(stream: FileInputStream, len: Long) {
    }
}
