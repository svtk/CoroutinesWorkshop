package com.kotlinconf.workshop.articles.exercises

import com.kotlinconf.workshop.articles.tasks.observeArticlesLoading
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SequentialTest {
    // Task: Write a test that collects the articles emitted by observeArticlesLoading and
    // asserts they match the expected articles, in order. ArticlesFakeData provides the mock
    // input data, and ArticlesFakeDataResults provides the expected output to assert against.
    @Test
    fun `test observeArticlesLoading Result`() = runTest {
        TODO()
    }

    // Task: Write a test that asserts the virtual time observeArticlesLoading takes to complete
    // matches the expected sequential duration. ArticlesFakeDataResults.expectedSequentialList
    // has a `duration` property you can compare against.
    @Test
    fun `test observeArticlesLoading Duration`() = runTest {
        TODO()
    }
}
