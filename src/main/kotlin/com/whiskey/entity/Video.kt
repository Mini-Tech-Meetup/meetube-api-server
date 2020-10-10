package com.whiskey.entity

import org.hibernate.annotations.Type
import javax.persistence.*

@Entity
@Table(name = "videos")
data class Video(
    @Id
    @Column(name = "id")
    val id: String,

    @Column(name = "title")
    val title: String,

    @Column(name = "keywords", columnDefinition = "text[]")
    @Type(type = "com.whiskey.entity.StringArrayType")
    val keywords: Array<String>? = null,

    @Column(name = "caption")
    val caption: String? = null,

    @Column(name = "description")
    val description: String? = null,

    @Column(name = "url")
    val url: String? = null,

    @Column(name = "thumbnail_url")
    val thumbnailUrl: String? = null
)