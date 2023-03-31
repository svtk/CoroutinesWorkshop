package com.kotlinconf.workshop.articles.tasks

import com.kotlinconf.workshop.articles.model.Article
import com.kotlinconf.workshop.articles.network.BlogService
import com.kotlinconf.workshop.blog.ArticleInfo
import com.kotlinconf.workshop.blog.Comment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry

fun observeArticlesUnstableWithRetry(service: BlogService): Flow<Article> = flow {
    val list = service.getArticleInfoList()
    for (articleInfo in list) {
        emit(Article(articleInfo, getCommentsWithRetry(service, articleInfo)))
    }
}

suspend fun getCommentsWithRetry(service: BlogService, articleInfo: ArticleInfo): List<Comment> {
    // initial code:
//    return getCommentsUnstable(articleInfo)
    val flow = flow {
        this.emit(service.getCommentsUnstable(articleInfo))
    }
    return flow.retry(retries = 5).first()
}
