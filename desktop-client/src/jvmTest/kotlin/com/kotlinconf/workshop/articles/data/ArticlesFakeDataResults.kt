package com.kotlinconf.workshop.articles.data

import com.kotlinconf.workshop.articles.model.Article
import com.kotlinconf.workshop.data.ArticlesFakeData
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

data class TestResults(val duration: Duration, val list: List<Article>)
object ArticlesFakeDataResults {
    private fun sequentialDuration() =
        ArticlesFakeData.getArticles()
            .sumOf { articleInfo -> ArticlesFakeData.getDelay(articleInfo.id).inWholeMilliseconds }.milliseconds

    private fun concurrentDuration() =
        ArticlesFakeData.getArticles()
            .maxOf { articleInfo -> ArticlesFakeData.getDelay(articleInfo.id) }

    private fun getArticles() =
        ArticlesFakeData.getArticles()
            .map { articleInfo -> Article(articleInfo, ArticlesFakeData.getComments(articleInfo.id)) }

    val expectedSequentialList by lazy {
        TestResults(
            duration = sequentialDuration(),
            list = getArticles()
        )
    }

    val expectedConcurrentList by lazy {
        TestResults(
            duration = concurrentDuration(),
            list = getArticles(),
        )
    }
}