package com.kotlinconf.workshop.articles.network

import com.kotlinconf.workshop.blog.ArticleInfo
import com.kotlinconf.workshop.blog.Comment
import com.kotlinconf.workshop.util.log
import io.ktor.client.call.*
import io.ktor.client.request.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

private interface BlogServiceRetrofit {
    @GET("articles")
    fun getArticlesCall(): Call<List<ArticleInfo>>

    @GET("articles/{articleId}/comments")
    fun getCommentsCall(
        @Path("articleId") articleId: Int,
    ): Call<List<Comment>>
}

class BlogServiceBlocking {
    private val retrofitService: BlogServiceRetrofit = WorkshopRetrofitClient.create(BlogServiceRetrofit::class.java)
    fun getArticleInfoList(): List<ArticleInfo> {
        log("Started loading articles (blocking)")
        return retrofitService.getArticlesCall().execute().body().orEmpty().also {
            log("Loaded articles (blocking)")
        }
    }

    fun getComments(articleInfo: ArticleInfo): List<Comment> {
        log("Started loading comments for article ${articleInfo.title} (blocking)")
        return retrofitService.getCommentsCall(articleInfo.id).execute().body().orEmpty().also {
            log("Loaded comments for article ${articleInfo.title} (blocking)")
        }
    }
}