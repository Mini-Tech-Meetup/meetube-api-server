package com.whiskey.controller

import com.whiskey.service.AzureVideoSerivce
import com.whiskey.service.VideoService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController("/upload")
@CrossOrigin("*")
class MediaController(
    private val azureVideoSerivce: AzureVideoSerivce,
    private val videoSerivce: VideoService
) {

    @PostMapping
    fun fileUpload(
        @RequestParam("file") file: MultipartFile
    ) {
        val data = azureVideoSerivce.fileUpload(file)
        videoSerivce.save(data)
    }
}
