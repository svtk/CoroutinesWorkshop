package com.kotlinconf.workshop.articles.data

import com.kotlinconf.workshop.articles.network.BlogService
import com.kotlinconf.workshop.articles.network.NetworkException
import com.kotlinconf.workshop.blog.ArticleInfo
import com.kotlinconf.workshop.data.ArticlesFakeData
import com.kotlinconf.workshop.blog.Comment
import kotlinx.coroutines.delay
import kotlin.random.Random

object MockBlogService: BlogService {
    override suspend fun getArticleInfoList(): List<ArticleInfo> {
        return ArticlesFakeData.getArticles()
    }

    override suspend fun getComments(articleInfo: ArticleInfo): List<Comment> {
        val id = articleInfo.id
        delay(ArticlesFakeData.getDelay(id))
        return ArticlesFakeData.getComments(id)
    }

    override suspend fun getCommentsUnstable(articleInfo: ArticleInfo): List<Comment> {
        if (Random.nextDouble() < 0.5) {
            throw NetworkException("Network error")
        }
        return getComments(articleInfo)
    }
}