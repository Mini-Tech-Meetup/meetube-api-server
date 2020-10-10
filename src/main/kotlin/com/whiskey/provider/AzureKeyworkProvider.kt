package com.whiskey.provider

import com.whiskey.client.AzureAiClient
import org.springframework.stereotype.Service

/**
 * Make a keyword extraction result provider
 */
@Service
class AzureKeyworkProvider(
    private val azureAiClient: AzureAiClient
)
