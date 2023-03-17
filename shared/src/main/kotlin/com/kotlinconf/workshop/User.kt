package com.kotlinconf.workshop

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String,
)

@Serializable
data class Issue(
    val id: Long,
    val title: String,
)

@Serializable
data class Comment(
    val id: Long,
    val issueId: Long,
    val author: User,
    val text: String,
    val timestamp: Long,
)