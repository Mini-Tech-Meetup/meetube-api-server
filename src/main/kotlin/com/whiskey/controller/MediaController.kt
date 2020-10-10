package com.whiskey.controller

import com.whiskey.entity.Video
import com.whiskey.service.AzureVideoSerivce
import com.whiskey.service.VideoService
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@CrossOrigin("*")
class MediaController(
    private val azureVideoSerivce: AzureVideoSerivce,
    private val videoSerivce: VideoService
) {

    @PostMapping("/upload")
    fun fileUpload(
        @RequestParam("file") file: MultipartFile
    ) {
        val data = azureVideoSerivce.fileUpload(file)
        videoSerivce.save(data)
    }



    @GetMapping("/video")
    fun video(
        @RequestParam("id") id:String
    ): Video {


        val data = videoSerivce.findById(id).get()

        if (data.description == null)
        {
            return azureVideoSerivce.getVideoIndexerInfomation(data)
                .let { videoSerivce.save(it)}
        }
        return data

    }


    @GetMapping("/videos")
    fun videos(): List<Video> {
        print("!!")
        return listOf(
            Video("123123123123","앗 자동차 타이어보다 싸다",null,null,null,"https://meetupmedia.blob.core.windows.net/meetup-media/Zoom Meetings.mp4"),
            Video("12312312s3123","앗 자동차 타이어보다 fsef",null,null,null,"https://meetupmedia.blob.core.windows.net/meetup-media/Zoom Meetings.mp4")
            ,Video("1231231233123","앗 자동차 타이어보다 w3dfsae",null,null,null,"https://meetupmedia.blob.core.windows.net/meetup-media/Zoom Meetings.mp4")
            ,Video("1231231as23123","앗 자동차 타이어보다 asdfef", arrayOf("sdaf","asf"),"sdfsdfasdf","Afewdsfasef","https://meetupmedia.blob.core.windows.net/meetup-media/Zoom Meetings.mp4")
        )
        val data = videoSerivce.findAll()
        val datas =  data.filterNot { it == null }.map { it.copy() }
        return datas
    }

    @GetMapping("/search")
    fun search(
        @RequestParam("condition") condition:String,
        @RequestParam("type") type :String
    ): List<Video> {

        if (type == "title")
        {
            return videoSerivce.findByTitlePartialMatching(condition)
        }
        else
        {
            val sets = condition.split(",").map { it.trim() }.toSet()
            return videoSerivce.findContainsKeywords(sets)
        }
    }
}
