package com.kotlinconf.workshop.tasks

import com.kotlinconf.workshop.model.Article
import com.kotlinconf.workshop.network.BlogService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.launch

suspend fun BlogService.loadArticlesConcurrentlyFlow(): Flow<List<Article>> {
    return channelFlow {
        val list = getArticleInfoList()
        for (articleInfo in list) {
            launch {
                send(Article(articleInfo, getComments(articleInfo)))
            }
        }
    }
        .runningFold(listOf()) { list, article -> list + article }
}

suspend fun BlogService.loadArticlesConcurrentlyWithProgressFlow(): Flow<List<Article>> {
    return loadArticlesConcurrentlyFlow()
        .runningFold(listOf()) { list, article -> list + article }
}

