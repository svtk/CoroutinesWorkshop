package com.kotlinconf.workshop.articles.tasks

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import com.kotlinconf.workshop.articles.data.ArticlesFakeDataResults
import com.kotlinconf.workshop.articles.data.MockBlogService
import com.kotlinconf.workshop.articles.model.Article
import com.kotlinconf.workshop.data.ArticlesFakeData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.measureTime

class Task4ProgressKtTest {
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

    @Test
    fun `test observeArticlesLoading Duration`() = runTest {
        val totalTime = testScheduler.timeSource.measureTime {
            observeArticlesLoading(MockBlogService).test {
                repeat(ArticlesFakeData.getArticles().size) { awaitItem() }
                awaitComplete()
            }
        }
        assertEquals(
            expected = ArticlesFakeDataResults.expectedSequentialList.duration,
            actual = totalTime,
            message = "Wrong total virtual time for 'observeArticlesLoading'"
        )
    }
}
