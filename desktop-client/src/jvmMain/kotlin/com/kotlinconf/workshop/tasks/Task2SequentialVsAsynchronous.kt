package com.kotlinconf.workshop.tasks

import com.kotlinconf.workshop.articles.Article
import com.kotlinconf.workshop.network.BlogService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

suspend fun BlogService.loadArticlesConcurrently(): List<Article> = coroutineScope {
    val articleInfoList = getArticleInfoList()
    val deferreds = articleInfoList.map { article ->
        async {
            Article(article, getComments(article))
        }
    }
    deferreds.awaitAll()
}