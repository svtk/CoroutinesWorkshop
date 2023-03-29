package com.kotlinconf.workshop.articles.tasks

import com.kotlinconf.workshop.articles.model.Article
import com.kotlinconf.workshop.articles.network.*
import com.kotlinconf.workshop.blog.ArticleInfo

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