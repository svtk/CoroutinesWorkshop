package com.kotlinconf.workshop.articles.tasks

import kotlinx.coroutines.test.runTest
import com.kotlinconf.workshop.articles.data.ArticlesFakeDataResults
import com.kotlinconf.workshop.articles.data.MockBlogService
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.measureTime

class Task2SequentialVsAsynchronousKtTest {
    @Test
    fun `test loadArticlesConcurrently Result`() = runTest {
        val result = loadArticlesConcurrently(MockBlogService)
        assertEquals(
            expected = ArticlesFakeDataResults.expectedConcurrentList.list,
            actual = result,
            message = "Wrong result for 'loadArticlesConcurrently'"
        )
    }

    @Test
    fun `test loadArticlesConcurrently Duration`() = runTest {
        val totalTime = testScheduler.timeSource.measureTime {
            loadArticlesConcurrently(MockBlogService)
        }
        assertEquals(
            expected = ArticlesFakeDataResults.expectedConcurrentList.duration,
            totalTime,
            "Wrong total virtual time for 'loadArticlesConcurrently'"
        )
    }
}