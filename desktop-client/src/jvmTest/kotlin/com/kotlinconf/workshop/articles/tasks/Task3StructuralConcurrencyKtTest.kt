package com.kotlinconf.workshop.articles.tasks

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import com.kotlinconf.workshop.articles.data.ArticlesFakeDataResults
import com.kotlinconf.workshop.articles.data.MockBlogService
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTime

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
    fun `test loadArticlesConcurrently Cancellation`() = runTest {
        val totalTime = testScheduler.timeSource.measureTime {
            val job = launch {
                loadArticlesConcurrently(MockBlogService)
            }
            delay(1.seconds)
            job.cancel()
            job.join()

            assertTrue(job.isCancelled, "Job should be cancelled")
        }
        val uncancelledDuration = ArticlesFakeDataResults.expectedConcurrentList.duration
        assertTrue(
            totalTime < uncancelledDuration,
            "Cancelling after 1s of virtual time should stop the remaining requests instead of " +
                    "letting loading run for the full $uncancelledDuration, but took $totalTime"
        )
    }
}
