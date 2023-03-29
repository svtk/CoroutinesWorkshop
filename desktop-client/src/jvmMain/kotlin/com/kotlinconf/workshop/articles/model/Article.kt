package com.kotlinconf.workshop.articles.model

import com.kotlinconf.workshop.blog.ArticleInfo
import com.kotlinconf.workshop.blog.Comment
import com.kotlinconf.workshop.blog.User

data class Article(
    val info: ArticleInfo,
    val comments: List<Comment>
) {
    val author: User
        get() = info.author
    val commenters: Set<User>
        get() = comments.map { it.author }.toSet()

    val activeUsers: Set<User>
        get() = commenters + author
}

fun List<Article>.findActiveUsers(): Set<User> =
    flatMap { it.activeUsers }.toSet()

