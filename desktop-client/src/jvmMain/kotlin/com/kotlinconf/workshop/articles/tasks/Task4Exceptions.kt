package com.kotlinconf.workshop.articles.tasks

import com.kotlinconf.workshop.blog.ArticleInfo
import com.kotlinconf.workshop.blog.Comment
import com.kotlinconf.workshop.articles.model.Article
import com.kotlinconf.workshop.articles.network.BlogService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*

suspend fun BlogService.loadArticlesUnstable(): List<Article> = coroutineScope {
    val articleInfoList = getArticleInfoList()
    val deferreds = articleInfoList.map { article ->
        async {
            Article(article, getCommentsWithRetry(article))
        }
    }
    deferreds.awaitAll()
}

suspend fun BlogService.getCommentsWithRetry(articleInfo: ArticleInfo): List<Comment> {
    // initial code:
//    return getCommentsUnstable(articleInfo)
    val flow = flow {
        emit(getCommentsUnstable(articleInfo))
    }
    return flow.retry(retries = 5).first()
}
