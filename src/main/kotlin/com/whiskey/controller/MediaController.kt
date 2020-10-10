package com.whiskey.controller

import com.whiskey.service.AzureVideoSerivce
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class MediaController(
    private val videoService: AzureVideoSerivce
) {

    @PostMapping("/upload")
    fun fileUpload(
        @RequestParam("file") file: MultipartFile
    ) {
        videoService.fileUpload(file)
    }

    @PostMapping("/uploadSuccess")
    fun fileUploadEnd(
        @RequestParam("uuid") uuid: String,
        @RequestParam("id") id: Int,
        @RequestParam("status") status: String
    ) {
        TODO()
    }

    @GetMapping("/upload")
    fun fileUpload() {
        print("asdfasdf")
    }
}
