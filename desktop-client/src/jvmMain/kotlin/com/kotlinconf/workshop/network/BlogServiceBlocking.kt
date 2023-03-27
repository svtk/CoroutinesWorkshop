package com.kotlinconf.workshop.network

import com.kotlinconf.workshop.blog.ArticleInfo
import com.kotlinconf.workshop.blog.Comment
import com.kotlinconf.workshop.util.log
import io.ktor.client.call.*
import io.ktor.client.request.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

interface BlogServiceBlocking {
    @GET("articles")
    fun getArticlesCall(): Call<List<ArticleInfo>>

    @GET("articles/{articleId}/comments")
    fun getCommentsCall(
        @Path("articleId") articleId: Int,
    ): Call<List<Comment>>
}

fun BlogServiceBlocking.getArticleInfoList(): List<ArticleInfo> {
    log("Started loading articles (blocking)")
    return getArticlesCall().execute().body().orEmpty().also {
        log("Loaded articles (blocking)")
    }
}

fun BlogServiceBlocking.getComments(articleInfo: ArticleInfo): List<Comment> {
    log("Started loading comments for article ${articleInfo.title} (blocking)")
    return getCommentsCall(articleInfo.id).execute().body().orEmpty().also {
        log("Loaded comments for article ${articleInfo.title} (blocking)")
    }
}

fun createBlogServiceBlocking(): BlogServiceBlocking {
    return WorkshopRetrofitClient.create(BlogServiceBlocking::class.java)
}
