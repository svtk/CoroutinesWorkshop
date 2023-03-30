package com.kotlinconf.workshop.coroutineBuilders

import com.kotlinconf.workshop.util.log
import kotlinx.coroutines.*

suspend fun main3() {
    val userId = service.login("", "")
    val blogInfo = service.loadBlogInfo()
    val articles = service.loadArticles(userId, blogInfo)
    println(articles)
}

// TODO: Dispatchers.Default
suspend fun main4() = coroutineScope {
    log("Start")
    val userIdDeferred = async(Dispatchers.Default) {
        service.login("", "")
    }
    val blogInfoDeferred = async {
        service.loadBlogInfo()
    }
    val articles = service.loadArticles(userIdDeferred.await(), blogInfoDeferred.await())
    println(articles)
}

suspend fun main() = coroutineScope {
    log("Start")
    val userIdDeferred = async(CoroutineName("login")) {
        service.login("", "")
    }
    val blogInfoDeferred = async(CoroutineName("bloginfo")) {
        service.loadBlogInfo()
    }
    val userId = userIdDeferred.await()
    val blogInfo = blogInfoDeferred.await()
    val articles = service.loadArticles(userId, blogInfo)
    println(articles)
}
