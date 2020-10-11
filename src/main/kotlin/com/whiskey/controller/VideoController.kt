package com.whiskey.controller

import com.whiskey.entity.Video
import com.whiskey.service.AzureVideoSerivce
import com.whiskey.service.VideoService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PathVariable

@RestController("/video")
@CrossOrigin("*")
class VideoController(
    private val azureVideoSerivce: AzureVideoSerivce,
    private val videoService: VideoService
) {
    @GetMapping("/video")
    fun getVideos(
        @RequestParam("title", required = false)
        title: String?,
        @RequestParam("keywords", required = false)
        keywords: Set<String>?
    ): List<Video> = title?.let { videoService.findByTitlePartialMatching(it) }
        ?: keywords?.let { videoService.findContainsKeywords(keywords) }
        ?: videoService.findAll()

    @GetMapping("/video/{id}")
    fun getVideoById(
        @PathVariable("id", required = true)
        id: String
    ): Video {
        val video = videoService.findById(id).get()

        return video.takeIf { it.description != null }
            ?: run {
                azureVideoSerivce.getVideoIndexerInfomation(video)
                videoService.save(video)
            }
    }
}
