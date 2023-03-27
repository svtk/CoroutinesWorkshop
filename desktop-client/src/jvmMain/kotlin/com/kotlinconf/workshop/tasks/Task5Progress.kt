package com.kotlinconf.workshop.tasks

import com.kotlinconf.workshop.model.Article
import com.kotlinconf.workshop.network.BlogService
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