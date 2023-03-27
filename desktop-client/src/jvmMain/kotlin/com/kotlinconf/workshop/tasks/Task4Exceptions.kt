package com.kotlinconf.workshop.tasks

import com.kotlinconf.workshop.blog.Comment
import com.kotlinconf.workshop.blog.ArticleInfo
import com.kotlinconf.workshop.articles.Article
import com.kotlinconf.workshop.network.BlogService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

suspend fun BlogService.loadArticlesUnstable(): List<Article> = coroutineScope {
    val articleInfoList = getArticleInfoList()
    val deferreds = articleInfoList.map { article ->
        async {
            Article(
                article,
                getCommentsUnstable(article)
            )
        }
    }
    deferreds.awaitAll()
}

// TODO
// Implement 'getCommentsWithRetry'
// (Discuss what happens if we don't catch CancellationException)
suspend fun BlogService.getCommentsWithRetry(articleInfo: ArticleInfo): List<Comment>? {
    var result: List<Comment>?
    repeat(5) {
        result = try {
            getCommentsUnstable(articleInfo)
        } catch (e: Exception) {
            //if (e is CancellationException) throw e
            null
        }
        if (result != null) return result
    }
    return null
}