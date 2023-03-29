package com.kotlinconf.workshop.articles.tasks

import com.kotlinconf.workshop.articles.model.Article
import com.kotlinconf.workshop.articles.network.BlogService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.runningFold

fun BlogService.observeArticlesLoading(): Flow<Article> = flow {
    val list = getArticleInfoList()
    for (articleInfo in list) {
        emit(Article(articleInfo, getComments(articleInfo)))
    }
}

suspend fun BlogService.loadArticlesWithProgress(): Flow<List<Article>> {
    return observeArticlesLoading()
        .runningFold(listOf()) { list, article -> list + article }
}