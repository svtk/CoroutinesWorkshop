package com.kotlinconf.workshop.articles.tasks

import com.kotlinconf.workshop.articles.model.Article
import com.kotlinconf.workshop.articles.network.BlogService
import kotlinx.coroutines.flow.*

fun observeArticlesUnstable(service: BlogService): Flow<Article> = flow {
    val list = service.getArticleInfoList()
    for (articleInfo in list) {
        emit(Article(articleInfo, service.getCommentsUnstable(articleInfo)))
    }
}