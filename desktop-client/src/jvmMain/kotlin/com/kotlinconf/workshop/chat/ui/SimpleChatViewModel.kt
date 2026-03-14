package com.kotlinconf.workshop.chat.ui

import com.kotlinconf.workshop.ChatMessage
import com.kotlinconf.workshop.chat.network.ChatService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SimpleChatViewModel(private val chatService: ChatService) {
    private val scope = CoroutineScope(SupervisorJob())

    val messages: StateFlow<List<ChatMessage>>
        field = MutableStateFlow(listOf())

    fun sendMessage(message: ChatMessage) {
        scope.launch {
            chatService.sendMessage(message)
        }
    }

    init {
        scope.launch {
            chatService.observeMessageEvents().collect { message ->
                messages.update { it + message }
            }
        }
    }
}
