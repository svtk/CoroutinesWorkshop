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
class Task1BlockVsSuspendKtTest {
    @Test
    fun `test loadArticles Result`() = runTest {
        val result = loadArticles(MockBlogService)
        assertEquals(
            expected = ArticlesFakeDataResults.expectedSequentialList.list,
            actual = result,
            message = "Wrong result for 'loadArticles'"
        )
    }

    @Test
    fun `test loadArticles Duration`() = runTest {

        val startTime = currentTime
        loadArticles(MockBlogService)
        val totalTime = (currentTime - startTime).milliseconds
        assertEquals(
            expected = ArticlesFakeDataResults.expectedSequentialList.duration,
            totalTime,
            "Wrong total virtual time for 'loadArticles'"
        )
    }
}