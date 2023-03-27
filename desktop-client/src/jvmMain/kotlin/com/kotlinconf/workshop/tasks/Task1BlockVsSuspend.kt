package com.kotlinconf.workshop.tasks

import com.kotlinconf.workshop.blog.ArticleInfo
import com.kotlinconf.workshop.network.*
import com.kotlinconf.workshop.model.Article

// TODO
// Implement 'loadArticlesWithComments' in two versions: blocking and suspend

fun BlogServiceBlocking.loadArticles(): List<Article> {
    val articleInfoList = getArticleInfoList()
    return articleInfoList.map { articleInfo: ArticleInfo ->
        Article(articleInfo, getComments(articleInfo))
    }
}

suspend fun BlogService.loadArticles(): List<Article> {
    val articleInfoList = getArticleInfoList()
    return articleInfoList.map { articleInfo: ArticleInfo ->
        Article(articleInfo, getComments(articleInfo))
    }
}

fun main1() {
    val serviceBlocking = createBlogServiceBlocking()
    val articles = serviceBlocking.loadArticles()
    println(articles)
}

suspend fun main() {
    val service = BlogService()
    println(service.loadArticles())
}