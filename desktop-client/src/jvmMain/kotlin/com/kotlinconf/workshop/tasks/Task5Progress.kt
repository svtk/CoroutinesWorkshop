package com.kotlinconf.workshop.tasks

import com.kotlinconf.workshop.blog.ArticleInfo
import com.kotlinconf.workshop.model.Article
import com.kotlinconf.workshop.network.BlogService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.runningFold

// TODO before:
// 1. implement observeTemperature
// 2. difference between flow and hot
// 3. from ws
// 4. from callbacks

suspend fun BlogService.observeArticlesLoading(): Flow<Article> {
    return getArticleInfoList()
        .asFlow()
        .map { articleInfo: ArticleInfo ->
            Article(articleInfo, getComments(articleInfo))
        }
}

suspend fun BlogService.loadArticlesWithProgress(): Flow<List<Article>> {
    return observeArticlesLoading()
        .runningFold(listOf()) { list, article -> list + article }
}