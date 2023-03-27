package com.kotlinconf.workshop.tasks

import com.kotlinconf.workshop.blog.ArticleInfo
import com.kotlinconf.workshop.blog.Comment
import com.kotlinconf.workshop.model.Article
import com.kotlinconf.workshop.network.BlogService
import kotlinx.coroutines.flow.*

suspend fun BlogService.loadArticlesWithProgressUnstable(): Flow<List<Article>> {
    return getArticleInfoList()
        .asFlow()
        .map { articleInfo: ArticleInfo ->
            Article(
                articleInfo,
                getCommentsUnstable(articleInfo))
        }
        .runningFold(listOf()) { list, article -> list + article }
}

suspend fun BlogService.getCommentsWithRetryV2(articleInfo: ArticleInfo): List<Comment> {
    val flow = flow {
        emit(getCommentsUnstable(articleInfo))
    }
    return flow.retry(retries = 5).first()
}
