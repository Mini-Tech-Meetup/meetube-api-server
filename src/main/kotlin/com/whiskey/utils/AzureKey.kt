package com.whiskey.utils

object AzureKey {
    val videoIndexerKey = "KIKI" //

    val StorageConnectionString = System.getenv("AZURE_STORAGE_CONNECTION_STRING")

    const val StorageContainerName = "meetup-media"

    val videoIndexerUserId = "c12fc22e-0dc9-466b-96e0-f77cd8a182e0"
}
