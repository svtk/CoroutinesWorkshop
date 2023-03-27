package com.kotlinconf.workshop.tasks

import com.kotlinconf.workshop.model.Article
import com.kotlinconf.workshop.network.BlogService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

// Run both versions and try to cancel
suspend fun BlogService.loadArticlesNonCancelable(): List<Article> = coroutineScope {
    val articleInfoList = getArticleInfoList()
    val deferreds = articleInfoList.map { article ->
        GlobalScope.async {
            Article(article, getComments(article))
        }
    }
    deferreds.awaitAll()
}