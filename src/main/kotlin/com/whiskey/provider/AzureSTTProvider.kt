package com.whiskey.provider

import com.microsoft.cognitiveservices.speech.*
import com.microsoft.cognitiveservices.speech.audio.AudioConfig
import org.springframework.stereotype.Service
import java.util.concurrent.Future


/**
 * Make a STT result provider
 */
@Service
class AzureSTTProvider
{
    val SPEECH__SUBSCRIPTION__KEY = System.getenv("SPEECH__SUBSCRIPTION__KEY")
    val SPEECH__SERVICE__REGION = System.getenv("SPEECH__SERVICE__REGION")
    fun main(){
        // setting
        val config: SpeechConfig = SpeechConfig.fromSubscription(SPEECH__SUBSCRIPTION__KEY, SPEECH__SERVICE__REGION)
        val audioConfig = AudioConfig.fromWavFileInput("file")
        val recognizer = SpeechRecognizer(config,"en-US", audioConfig)
        val task: Future<SpeechRecognitionResult> = recognizer.recognizeOnceAsync()
        val result: SpeechRecognitionResult = task.get()


        when (result.reason) {
            ResultReason.RecognizedSpeech -> {
                println("We recognized: " + result.text)
            }
            ResultReason.NoMatch -> println("NOMATCH: Speech could not be recognized.")
            ResultReason.Canceled -> {
                val cancellation = CancellationDetails.fromResult(result)
                println("CANCELED: Reason=" + cancellation.reason)
                if (cancellation.reason == CancellationReason.Error) {
                    println("CANCELED: ErrorCode=" + cancellation.errorCode)
                    println("CANCELED: ErrorDetails=" + cancellation.errorDetails)
                    println("CANCELED: Did you update the subscription info?")
                }
            }
        }
    }
}