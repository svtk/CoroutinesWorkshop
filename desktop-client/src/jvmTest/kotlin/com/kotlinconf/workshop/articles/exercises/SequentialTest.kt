package com.kotlinconf.workshop.articles.exercises

import app.cash.turbine.test
import com.kotlinconf.workshop.articles.data.ArticlesFakeDataResults
import com.kotlinconf.workshop.articles.data.MockBlogService
import com.kotlinconf.workshop.articles.model.Article
import com.kotlinconf.workshop.articles.tasks.observeArticlesLoading
import com.kotlinconf.workshop.data.ArticlesFakeData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class SequentialTest {
    // Task: Write a test that collects the articles emitted by observeArticlesLoading and
    // asserts they match the expected articles, in order. ArticlesFakeData provides the mock
    // input data, and ArticlesFakeDataResults provides the expected output to assert against.
    @Test
    fun `test observeArticlesLoading Result`() = runTest {
        val results = mutableListOf<Article>()
        observeArticlesLoading(MockBlogService).test {
            repeat(ArticlesFakeData.getArticles().size) {
                results += awaitItem()
            }
            awaitComplete()
        }
        assertEquals(
            expected = ArticlesFakeDataResults.expectedSequentialList.list,
            actual = results,
            message = "Wrong emission order/result for 'observeArticlesLoading' " +
                    "(loading is sequential, so articles should arrive in list order)"
        )
    }

    // Task: Write a test that asserts the virtual time observeArticlesLoading takes to complete
    // matches the expected sequential duration. ArticlesFakeDataResults.expectedSequentialList
    // has a `duration` property you can compare against.
    @Test
    fun `test observeArticlesLoading Duration`() = runTest {
        val startTime = currentTime
        observeArticlesLoading(MockBlogService).test {
            repeat(ArticlesFakeData.getArticles().size) { awaitItem() }
            awaitComplete()
        }
        val totalTime = (currentTime - startTime).milliseconds
        assertEquals(
            expected = ArticlesFakeDataResults.expectedSequentialList.duration,
            actual = totalTime,
            message = "Wrong total virtual time for 'observeArticlesLoading'"
        )
    }
}
