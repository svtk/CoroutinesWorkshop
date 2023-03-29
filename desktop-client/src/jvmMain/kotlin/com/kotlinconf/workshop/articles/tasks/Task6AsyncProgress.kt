package com.kotlinconf.workshop.articles.tasks

import com.kotlinconf.workshop.articles.model.Article
import com.kotlinconf.workshop.articles.network.BlogService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.launch

fun BlogService.loadArticlesConcurrentlyFlow(): Flow<Article> {
    return channelFlow {
        val list = getArticleInfoList()
        for (articleInfo in list) {
            launch {
                send(Article(articleInfo, getComments(articleInfo)))
            }
        }
    }
}

fun BlogService.loadArticlesConcurrentlyWithProgressFlow(): Flow<List<Article>> {
    return loadArticlesConcurrentlyFlow()
        .runningFold(listOf()) { list, article -> list + article }
}

