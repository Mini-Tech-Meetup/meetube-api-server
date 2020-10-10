package com.whiskey.utils

object AzureKey {
    val StorageConnectionString = System.getenv("AZURE_STORAGE_CONNECTION_STRING")
    const val StorageContainerName = "meetup-media"
}