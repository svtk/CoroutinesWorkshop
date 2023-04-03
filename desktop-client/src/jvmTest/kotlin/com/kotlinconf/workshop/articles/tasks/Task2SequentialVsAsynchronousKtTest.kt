package com.kotlinconf.workshop.articles.tasks

import com.kotlinconf.workshop.articles.data.ArticlesFakeDataResults
import com.kotlinconf.workshop.articles.data.MockBlogService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
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
        val startTime = currentTime
        loadArticlesConcurrently(MockBlogService)
        val totalTime = (currentTime - startTime).milliseconds
        assertEquals(
            expected = ArticlesFakeDataResults.expectedConcurrentList.duration,
            totalTime,
            "Wrong total virtual time for 'loadArticlesConcurrently'"
        )
    }
}