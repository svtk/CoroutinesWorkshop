package com.kotlinconf.workshop.articles.exercises

import app.cash.turbine.test
import com.kotlinconf.workshop.articles.data.ArticlesFakeDataResults
import com.kotlinconf.workshop.articles.data.MockBlogService
import com.kotlinconf.workshop.articles.model.Article
import com.kotlinconf.workshop.articles.tasks.observeArticlesConcurrently
import com.kotlinconf.workshop.data.ArticlesFakeData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AsyncProgressTurbineTest {
    // Task: Using Turbine, write a test that collects the articles emitted by
    // observeArticlesConcurrently and asserts that all expected articles arrive (order isn't
    // guaranteed for concurrent loading). ArticlesFakeData provides the mock input data, and
    // ArticlesFakeDataResults provides the expected output to assert against.
    @Test
    fun `test observeArticlesConcurrently Result`() = runTest {
        val results = mutableListOf<Article>()
        observeArticlesConcurrently(MockBlogService).test {
            repeat(ArticlesFakeData.getArticles().size) {
                results += awaitItem()
            }
            awaitComplete()
        }
        assertEquals(
            expected = ArticlesFakeDataResults.expectedConcurrentList.list.toSet(),
            actual = results.toSet(),
            message = "Wrong set of emitted articles for 'observeArticlesConcurrently' " +
                    "(order isn't guaranteed when loading concurrently, but all articles should still arrive)"
        )
    }

    // Task: Write a test that asserts the virtual time observeArticlesConcurrently takes to
    // complete stays within the expected upper bound for concurrent loading.
    // ArticlesFakeDataResults.expectedConcurrentList has a `duration` property you can compare
    // against.
    @Test
    fun `test observeArticlesConcurrently Duration`() = runTest {
        val startTime = currentTime
        observeArticlesConcurrently(MockBlogService).test {
            repeat(ArticlesFakeData.getArticles().size) { awaitItem() }
            awaitComplete()
        }
        val totalTime = currentTime - startTime
        val upperBound = ArticlesFakeDataResults.expectedConcurrentList.duration.inWholeMilliseconds
        assertTrue(
            totalTime <= upperBound,
            "Concurrent loading took $totalTime ms of virtual time, expected at most $upperBound ms"
        )
    }
}
