package com.kotlinconf.workshop.articles.tasks

import com.kotlinconf.workshop.articles.model.Article
import com.kotlinconf.workshop.articles.network.BlogService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

fun observeArticlesConcurrently(service: BlogService): Flow<Article> {
    return channelFlow {
        val list = service.getArticleInfoList()
        for (articleInfo in list) {
            launch {
                send(Article(articleInfo, service.getComments(articleInfo)))
            }
        }
    }
}
