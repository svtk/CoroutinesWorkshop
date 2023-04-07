package com.kotlinconf.workshop

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(val content: String)

fun ChatMessage.isImportant() =
    content.contains("@channel") || content.contains("@here")