package com.kotlinconf.workshop

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(val content: String)

