package com.kotlinconf.workshop.chat.ui

import com.kotlinconf.workshop.ChatMessage
import com.kotlinconf.workshop.chat.network.ChatService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ChatViewModel(val chatService: ChatService) {
    private val scope = CoroutineScope(SupervisorJob())

    private val _importantMessages = MutableStateFlow<List<ChatMessage>>(listOf())
    val importantMessages: MutableStateFlow<List<ChatMessage>> get() = _importantMessages

    private val _allOtherMessages = MutableStateFlow<List<ChatMessage>>(listOf())
    val allOtherMessages: MutableStateFlow<List<ChatMessage>> get() = _allOtherMessages

    fun sendMessage(message: ChatMessage) {
        scope.launch {
            chatService.sendMessage(message)
        }
    }

    init {
        scope.launch {
            chatService.observeMessageEvents().collect { message ->
                if (message.content.contains("@channel")) {
                    _importantMessages.update { it + message }
                } else {
                    _allOtherMessages.update { it + message }
                }
            }
        }
    }
}