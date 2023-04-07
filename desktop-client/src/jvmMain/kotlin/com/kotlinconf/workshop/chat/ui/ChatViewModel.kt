package com.kotlinconf.workshop.chat.ui

import com.kotlinconf.workshop.FeedMessage
import com.kotlinconf.workshop.PrivateMessage
import com.kotlinconf.workshop.chat.network.ChatService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ChatViewModel(chatService: ChatService) {
    val scope = CoroutineScope(SupervisorJob())

    val _privateMessages = MutableStateFlow<List<PrivateMessage>>(listOf())
    val privateMessages: MutableStateFlow<List<PrivateMessage>> get() = _privateMessages

    val _feedMessages = MutableStateFlow<List<FeedMessage>>(listOf())
    val feedMessages: MutableStateFlow<List<FeedMessage>> get() = _feedMessages

    init {
        scope.launch {
            chatService.observeMessageEvents().collect { message ->
                when (message) {
                    is PrivateMessage -> privateMessages.update { it + message }
                    is FeedMessage -> feedMessages.update { it + message }
                }
            }
        }
    }
}