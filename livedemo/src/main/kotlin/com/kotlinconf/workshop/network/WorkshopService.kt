package com.kotlinconf.workshop.network

import com.kotlinconf.workshop.blog.ArticleInfo
import com.kotlinconf.workshop.util.log
import kotlinx.coroutines.delay

// TODO network
data class UserID(val id: Long)
data class UserData(val id: UserID, val name: String)

data class BlogInfo(val title: String)

interface WorkshopService {
    suspend fun login(username: String, password: String): UserID
    suspend fun loadBlogInfo(): BlogInfo

    suspend fun loadBlogInfoFailing(): BlogInfo
    suspend fun loadArticles(userId: UserID, blogInfo: BlogInfo): List<ArticleInfo>
}

class WorkshopServiceImpl : WorkshopService {
    override suspend fun login(username: String, password: String): UserID {
        log("Login $username starts")
        delay(1000)
        return UserID(0)
            .also { log("Login: user logged with $it") }
    }

    override suspend fun loadBlogInfo(): BlogInfo {
        log("Loading blog info starts")
        delay(500)
        return BlogInfo("Kotlin")
            .also { log("Loading blog info completes: $it") }
    }

    override suspend fun loadBlogInfoFailing(): BlogInfo {
        log("Loading blog info starts")
        delay(500)
        throw Exception("Loading error")
    }

    override suspend fun loadArticles(userId: UserID, blogInfo: BlogInfo): List<ArticleInfo> {
        log("Loading articles starts")
        delay(1000)
        return listOf<ArticleInfo>()
            .also { log("Loading articles completes: loaded ${it.size} articles") }
    }
}