package com.kotlinconf.workshop.articles.tasks

import com.kotlinconf.workshop.articles.model.Article
import com.kotlinconf.workshop.articles.network.BlogService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

// Run both versions and try to cancel
suspend fun BlogService.loadArticlesNonCancelable(): List<Article> {
    val articleInfoList = getArticleInfoList()
    val deferreds = articleInfoList.map { article ->
        GlobalScope.async {
            Article(article, getComments(article))
        }
    }
    return deferreds.awaitAll()
}