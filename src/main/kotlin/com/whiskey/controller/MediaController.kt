package com.whiskey.controller

import com.whiskey.entity.Video
import com.whiskey.service.AzureVideoSerivce
import com.whiskey.service.VideoService
import org.springframework.web.bind.annotation.*
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
