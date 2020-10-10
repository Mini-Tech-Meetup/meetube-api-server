package com.whiskey.service

import com.whiskey.entity.Video
import com.whiskey.repository.VideoRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class VideoService(
    private val videoRepository: VideoRepository
) {
    fun save(video: Video): Video = videoRepository.save(video)

    fun findByTitleFullMatching(title: String) = videoRepository.findByTitleFullMatching(title)
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    fun findById(id: String) = videoRepository.findById(id)
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    fun findAll() = videoRepository.findAll()

    fun findByTitlePartialMatching(title: String) = videoRepository.findByTitlePartialMatching(title)

    fun findContainsKeywords(keywords: Set<String>): List<Video> = videoRepository.findContainsKeywords(keywords)

}
