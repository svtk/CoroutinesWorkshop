package com.kotlinconf.workshop

import kotlinx.serialization.Serializable

@Serializable
data class Comment(val author: User, val content: String)