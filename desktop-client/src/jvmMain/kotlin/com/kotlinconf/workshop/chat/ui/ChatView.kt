package com.kotlinconf.workshop.chat.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.kotlinconf.workshop.ChatMessage

@Composable
fun ChatView(chatViewModel: ChatViewModel) {
    val feedMessages = chatViewModel.feedMessages.collectAsState()
    val privateMessages = chatViewModel.privateMessages.collectAsState()

    Row {
        MessageList(feedMessages.value)
        MessageList(privateMessages.value)
    }
}

@Composable
fun MessageList(chatMessages: List<ChatMessage>) {
    LazyColumn {
        items(chatMessages) { message ->
            Message(message)
        }
    }
}

@Composable
fun Message(chatMessage: ChatMessage) {
    Text(text = chatMessage.content)
}