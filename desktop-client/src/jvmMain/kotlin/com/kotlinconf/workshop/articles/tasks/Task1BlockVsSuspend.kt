package com.kotlinconf.workshop.articles.tasks

import com.kotlinconf.workshop.articles.model.Article
import com.kotlinconf.workshop.articles.network.BlogService
import com.kotlinconf.workshop.articles.network.BlogServiceBlocking
import com.kotlinconf.workshop.articles.network.createBlogService
import com.kotlinconf.workshop.blog.ArticleInfo

// TODO
// Implement 'loadArticlesWithComments' in two versions: blocking and suspend

// This function is invoked when you select the "BLOCKING" option in the UI.
fun loadArticles(serviceBlocking: BlogServiceBlocking): List<Article> {
    val articleInfoList = serviceBlocking.getArticleInfoList()
    return articleInfoList.map { articleInfo: ArticleInfo ->
        Article(articleInfo, serviceBlocking.getComments(articleInfo))
    }
}

// This function is invoked when you select the "SUSPENDING" option in the UI.
suspend fun loadArticles(service: BlogService): List<Article> {
    val articleInfoList = service.getArticleInfoList()
    return articleInfoList.map { articleInfo: ArticleInfo ->
        Article(articleInfo, service.getComments(articleInfo))
    }
}

fun main1() {
    val serviceBlocking = BlogServiceBlocking()
    val articles = loadArticles(serviceBlocking)
    println(articles)
}

suspend fun main() {
    val service = createBlogService()
    println(loadArticles(service))
}