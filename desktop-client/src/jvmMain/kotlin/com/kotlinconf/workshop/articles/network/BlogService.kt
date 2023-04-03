package com.kotlinconf.workshop.articles.network

import com.kotlinconf.workshop.WorkshopServerConfig.articlesEndpoint
import com.kotlinconf.workshop.WorkshopServerConfig.commentsEndpoint
import com.kotlinconf.workshop.WorkshopServerConfig.commentsUnstableEndpoint
import com.kotlinconf.workshop.blog.ArticleInfo
import com.kotlinconf.workshop.blog.Comment
import com.kotlinconf.workshop.util.log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

interface BlogService {
    suspend fun getArticleInfoList(): List<ArticleInfo>

    suspend fun getComments(articleInfo: ArticleInfo): List<Comment>

    suspend fun getCommentsUnstable(articleInfo: ArticleInfo): List<Comment>
}

fun createBlogService(): BlogService = BlogServiceImpl()

private class BlogServiceImpl: BlogService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    override suspend fun getArticleInfoList(): List<ArticleInfo> {
        log("Started loading articles")
        return client.get(articlesEndpoint)
            .body<List<ArticleInfo>>()
            .also { log("Loaded articles") }
    }

    override suspend fun getComments(articleInfo: ArticleInfo): List<Comment> {
        log("Started loading comments for article ${articleInfo.title}")
        return client.get(commentsEndpoint(articleInfo.id))
            .body<List<Comment>>()
            .also {
                log("Loaded comments for article ${articleInfo.title}")
            }
    }

    override suspend fun getCommentsUnstable(articleInfo: ArticleInfo): List<Comment> {
        log("Started loading comments for article ${articleInfo.title}")
        val response = client.get(commentsUnstableEndpoint(articleInfo.id))
        if (!response.status.isSuccess()) {
            log("Loaded comments failure for article ${articleInfo.title}: ${response.status}")
            throw NetworkException("Loading article ${articleInfo.id} was unsuccessful.")
        }
        return response.body<List<Comment>>()
            .also {
                log("Loaded comments unstable for article ${articleInfo.title}")
            }
    }
}

class NetworkException(message: String) : Exception(message)