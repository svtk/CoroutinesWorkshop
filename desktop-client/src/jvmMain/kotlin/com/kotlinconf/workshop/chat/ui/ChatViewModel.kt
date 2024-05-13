package com.kotlinconf.workshop.chat.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinconf.workshop.ChatMessage
import com.kotlinconf.workshop.chat.network.ChatService
import com.kotlinconf.workshop.isImportant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ChatViewModel(private val chatService: ChatService) : ViewModel() {
    // Task: Split chat into two StateFlows

    val importantMessages: StateFlow<List<ChatMessage>>
        field = MutableStateFlow(listOf())

    val allOtherMessages: StateFlow<List<ChatMessage>>
        field = MutableStateFlow(listOf())

    fun sendMessage(message: ChatMessage) {
        viewModelScope.launch {
            chatService.sendMessage(message)
        }
    }

    init {
        viewModelScope.launch {
            // TODO
        }
    }
}
