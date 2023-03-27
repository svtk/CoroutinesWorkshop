package com.kotlinconf.workshop.blog

import kotlinx.serialization.Serializable

@Serializable
data class ArticleInfo(
    val id: Int,
    val author: User,
    val title: String
)

@Serializable
data class User(
    val id: Int,
    val name: String
)

@Serializable
data class Comment(
    val author: User,
    val content: String
)

