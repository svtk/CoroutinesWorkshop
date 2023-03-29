package com.kotlinconf.workshop

import com.kotlinconf.workshop.network.WorkshopServiceImpl
import com.kotlinconf.workshop.util.log
import kotlinx.coroutines.*



suspend fun main0() {
    val workshopService = WorkshopServiceImpl()
    val userId = workshopService.login("", "")
    val blogInfo = workshopService.loadBlogInfo()
    val articles = workshopService.loadArticles(userId, blogInfo)
    println(articles)
}

suspend fun main1() {
    val scope = CoroutineScope(Dispatchers.Default)
    val workshopService = WorkshopServiceImpl()
    val userIdDeferred = scope.async {
        workshopService.login("", "")
    }
    val blogInfoDeferred = scope.async {
        workshopService.loadBlogInfo()
    }
    val userId = userIdDeferred.await()
    val blogInfo = blogInfoDeferred.await()
    val articles = workshopService.loadArticles(userId, blogInfo)
    println(articles)
}

suspend fun main2() {
    log("Start")
    val scope = CoroutineScope(Dispatchers.Default)
    val workshopService = WorkshopServiceImpl()
    val userIdDeferred = scope.async(CoroutineName("login")) {
        workshopService.login("", "")
    }
    val blogInfoDeferred = scope.async(CoroutineName("bloginfo")) {
        workshopService.loadBlogInfo()
    }
    val userId = userIdDeferred.await()
    val blogInfo = blogInfoDeferred.await()
    val articles = workshopService.loadArticles(userId, blogInfo)
    println(articles)
}

suspend fun main() = coroutineScope {
    log("Start")
    val workshopService = WorkshopServiceImpl()
    val userIdDeferred = async(CoroutineName("login")) {
        workshopService.login("", "")
    }
    val blogInfoDeferred = async(CoroutineName("bloginfo")) {
        workshopService.loadBlogInfo()
    }
    val userId = userIdDeferred.await()
    val blogInfo = blogInfoDeferred.await()
    val articles = workshopService.loadArticles(userId, blogInfo)
    println(articles)
}
