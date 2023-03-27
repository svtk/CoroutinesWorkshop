package com.kotlinconf.workshop.tasks

import com.kotlinconf.workshop.model.Article
import com.kotlinconf.workshop.network.BlogService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.launch

suspend fun BlogService.loadArticlesConcurrentlyWithProgress(): Flow<List<Article>> {
    return channelFlow {
        getArticleInfoList()
            .map { article ->
                launch {
                    this@channelFlow.send(Article(article, getComments(article)))
                }
            }
    }
        .runningFold(listOf()) { list, article -> list + article }
}
