package com.kotlinconf.workshop

import kotlinx.serialization.Serializable

@Serializable
sealed class ChatMessage(val content: String)

class PrivateMessage(content: String) : ChatMessage(content)
class FeedMessage(content: String) : ChatMessage(content)