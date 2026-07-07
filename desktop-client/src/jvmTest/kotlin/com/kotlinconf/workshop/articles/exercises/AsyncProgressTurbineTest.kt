package com.kotlinconf.workshop.articles.exercises

import com.kotlinconf.workshop.articles.tasks.observeArticlesConcurrently
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AsyncProgressTurbineTest {
    // Task: Using Turbine, write a test that collects the articles emitted by
    // observeArticlesConcurrently and asserts that all expected articles arrive (order isn't
    // guaranteed for concurrent loading). ArticlesFakeData provides the mock input data, and
    // ArticlesFakeDataResults provides the expected output to assert against.
    @Test
    fun `test observeArticlesConcurrently Result`() = runTest {
        TODO()
    }

    // Task: Write a test that asserts the virtual time observeArticlesConcurrently takes to
    // complete stays within the expected upper bound for concurrent loading.
    // ArticlesFakeDataResults.expectedConcurrentList has a `duration` property you can compare
    // against.
    @Test
    fun `test observeArticlesConcurrently Duration`() = runTest {
        TODO()
    }
}
