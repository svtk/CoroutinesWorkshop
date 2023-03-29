package com.kotlinconf.workshop.articles.tasks

import com.kotlinconf.workshop.articles.model.Article
import com.kotlinconf.workshop.articles.network.BlogService
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