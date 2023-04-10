package com.kotlinconf.workshop.chat.ui

import com.kotlinconf.workshop.ChatMessage
import com.kotlinconf.workshop.chat.network.ChatService
import com.kotlinconf.workshop.isImportant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ChatViewModel(private val chatService: ChatService) {
    private val scope = CoroutineScope(SupervisorJob())

    // Task: Split chat into two StateFlows

    private val _importantMessages = MutableStateFlow<List<ChatMessage>>(listOf())
    val importantMessages: StateFlow<List<ChatMessage>> get() = _importantMessages

    private val _allOtherMessages = MutableStateFlow<List<ChatMessage>>(listOf())
    val allOtherMessages: StateFlow<List<ChatMessage>> get() = _allOtherMessages

    fun sendMessage(message: ChatMessage) {
        scope.launch {
            chatService.sendMessage(message)
        }
    }

    init {
        scope.launch {
            chatService.observeMessageEvents().collect { message ->
                if (message.isImportant()) {
                    _importantMessages.update { it + message }
                } else {
                    _allOtherMessages.update { it + message }
                }
            }
        }
    }
}