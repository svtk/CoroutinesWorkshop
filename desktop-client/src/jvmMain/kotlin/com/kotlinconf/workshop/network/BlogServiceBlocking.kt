package com.kotlinconf.workshop.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kotlinconf.workshop.WorkshopServerConfig
import com.kotlinconf.workshop.blog.ArticleInfo
import com.kotlinconf.workshop.blog.Comment
import com.kotlinconf.workshop.util.log
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
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

@OptIn(ExperimentalSerializationApi::class)
fun createBlogServiceBlocking(): BlogServiceBlocking {
    val httpClient = OkHttpClient.Builder().build()

    val contentType = "application/json".toMediaType()
    val json = Json { ignoreUnknownKeys = true }
    val retrofit = Retrofit.Builder()
        .baseUrl(WorkshopServerConfig.WORKSHOP_SERVER_URL)
        .addConverterFactory(json.asConverterFactory(contentType))
        .client(httpClient)
        .build()
    return retrofit.create(BlogServiceBlocking::class.java)
}
