package com.kotlinconf.workshop.chat.ui

import com.kotlinconf.workshop.ChatMessage
import com.kotlinconf.workshop.chat.network.ChatService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ChatViewModel(val chatService: ChatService) {
    val scope = CoroutineScope(SupervisorJob())

    val _mentions = MutableStateFlow<List<ChatMessage>>(listOf())
    val mentions: MutableStateFlow<List<ChatMessage>> get() = _mentions

    val _chatMessages = MutableStateFlow<List<ChatMessage>>(listOf())
    val chatMessages: MutableStateFlow<List<ChatMessage>> get() = _chatMessages

    fun sendMessage(message: ChatMessage) {
        scope.launch {
            chatService.sendMessage(message)
        }
    }

    init {
        scope.launch {
            chatService.observeMessageEvents().collect { message ->
                if (message.content.contains("@channel")) {
                    _mentions.update { it + message }
                } else {
                    _chatMessages.update { it + message }
                }
            }
        }
    }
}