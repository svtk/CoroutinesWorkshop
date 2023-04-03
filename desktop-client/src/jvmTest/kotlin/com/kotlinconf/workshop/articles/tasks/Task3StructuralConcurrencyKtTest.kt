package com.kotlinconf.workshop.articles.tasks

import com.kotlinconf.workshop.articles.data.ArticlesFakeDataResults
import com.kotlinconf.workshop.articles.data.MockBlogService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class Task3StructuralConcurrencyKtTest {
    @Test
    fun `test loadArticlesNonCancelable Result`() = runTest {
        val result = loadArticlesNonCancelable(MockBlogService)
        assertEquals(
            expected = ArticlesFakeDataResults.expectedConcurrentList.list,
            actual = result,
            message = "Wrong result for 'loadArticlesNonCancelable'"
        )
    }

    @Test
    fun `test loadArticlesNonCancelable Duration`() = runTest {
        val startTime = currentTime
        loadArticlesNonCancelable(MockBlogService)
        val totalTime = (currentTime - startTime).milliseconds
        assertEquals(
            expected = ArticlesFakeDataResults.expectedConcurrentList.duration,
            totalTime,
            "Wrong total virtual time for 'loadArticlesNonCancelable'"
        )
    }

    @Test
    fun `test loadArticlesConcurrently Cancellation`() = runTest {
        val job = launch {
            loadArticlesConcurrently(MockBlogService)
        }
        delay(1000)
        job.cancel()
        // TODO Test that the articles loading was cancelled?
    }
}