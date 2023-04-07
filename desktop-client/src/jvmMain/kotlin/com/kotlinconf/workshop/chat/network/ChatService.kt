package com.kotlinconf.workshop.chat.network

import com.kotlinconf.workshop.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatService {
    suspend fun sendMessage(chatMessage: ChatMessage)
    fun observeMessageEvents(): Flow<ChatMessage>
}