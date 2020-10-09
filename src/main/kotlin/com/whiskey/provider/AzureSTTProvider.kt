package com.whiskey.provider

import com.whiskey.client.AzureAiClient
import org.springframework.stereotype.Service

/**
 * Make a STT result provider
 */
@Service
class AzureSTTProvider(
    private val azureAiClient: AzureAiClient
)