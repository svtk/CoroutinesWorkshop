package com.kotlinconf.workshop.articles.tasks

import com.kotlinconf.workshop.articles.model.Article
import com.kotlinconf.workshop.articles.network.*
import com.kotlinconf.workshop.blog.ArticleInfo

// TODO
// Implement 'loadArticlesWithComments' in two versions: blocking and suspend

fun loadArticles(serviceBlocking: BlogServiceBlocking): List<Article> {
    val articleInfoList = serviceBlocking.getArticleInfoList()
    return articleInfoList.map { articleInfo: ArticleInfo ->
        Article(articleInfo, serviceBlocking.getComments(articleInfo))
    }
}

suspend fun loadArticles(service: BlogService): List<Article> {
    val articleInfoList = service.getArticleInfoList()
    return articleInfoList.map { articleInfo: ArticleInfo ->
        Article(articleInfo, service.getComments(articleInfo))
    }
}

fun main1() {
    val serviceBlocking = createBlogServiceBlocking()
    val articles = loadArticles(serviceBlocking)
    println(articles)
}

suspend fun main() {
    val service = BlogService()
    println(loadArticles(service))
}