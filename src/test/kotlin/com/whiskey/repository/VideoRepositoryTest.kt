package com.whiskey.repository

import com.whiskey.entity.Video
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import javax.sql.DataSource

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
class VideoRepositoryTest(
    private val videoRepository: VideoRepository,
    private val dataSource: DataSource
) {
    private lateinit var flyway: Flyway

    @BeforeAll
    fun before() {
        flyway = Flyway.configure().dataSource(dataSource).load()
        flyway.clean()
        flyway.migrate()

        (1L..10L).forEach {
            val id = it.toString()
            videoRepository.save(
                Video(
                    id = "test_01_$id",
                    title = "test_01_$id",
                    keywords = arrayOf(id)
                )
            )
        }

        (1L..4L).forEach {
            val id = it.toString()
            videoRepository.save(
                Video(
                    id = "test_02_$id",
                    title = "test_02_$id",
                    keywords = arrayOf(id)
                )
            )
        }

        (1L..6L).forEach {
            val id = it.toString()
            videoRepository.save(
                Video(
                    id = "test_03_$id",
                    title = "test_03_$id",
                    keywords = arrayOf(id)
                )
            )
        }
    }

    @Test
    fun findAll() {
        val videos = videoRepository.findAll()
        assertEquals(20, videos.size)
    }

    @Test
    fun findByTitlePartialMatching() {
        assertEquals(10, videoRepository.findByTitlePartialMatching("test_01").size)
        assertEquals(4, videoRepository.findByTitlePartialMatching("test_02").size)
        assertEquals(6, videoRepository.findByTitlePartialMatching("test_03").size)
    }

    @Test
    fun findContainsKeywords() {
        val videos = videoRepository.findContainsKeywords(setOf("1"))
        assertEquals(3, videos.size)
    }
}
