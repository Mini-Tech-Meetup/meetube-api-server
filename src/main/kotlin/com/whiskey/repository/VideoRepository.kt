package com.whiskey.repository

import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.whiskey.entity.Video
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import com.whiskey.entity.QVideo.video as QVideo

@Repository
class VideoRepository(
    private val jpaQueryFactory: JPAQueryFactory,
    private val videoJpaRepository: VideoJpaRepository
) : VideoJpaRepository by videoJpaRepository {
    fun findContainsKeywords(keywords: Set<String>): List<Video> {
        val query = prepareQuery()
        query.where(
            Expressions.booleanTemplate(
                "FUNCTION('fn_array_overlaps', {0}, string_to_array({1}, ','))=true",
                QVideo.keywords,
                keywords.joinToString(",")
            )
        )
        return query.fetch()
    }

    fun findByTitleFullMatching(title: String): Video? {
        val query = prepareQuery()
        query.where(QVideo.title.like(title))
        return query.fetchOne()
    }

    fun findByTitlePartialMatching(title: String): List<Video> {
        val query = prepareQuery()
        query.where(QVideo.title.contains(title))
        return query.fetch()
    }

    private fun prepareQuery() = jpaQueryFactory.selectFrom(QVideo)
}

interface VideoJpaRepository : JpaRepository<Video, String>
