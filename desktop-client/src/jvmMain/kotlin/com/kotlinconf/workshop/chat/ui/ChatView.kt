package com.kotlinconf.workshop.chat.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kotlinconf.workshop.ChatMessage

@Composable
fun ChatView(chatViewModel: ChatViewModel) {
    val importantMessages = chatViewModel.importantMessages.collectAsState().value
    val allOtherMessages = chatViewModel.allOtherMessages.collectAsState().value
    ChatView(
        importantMessages,
        allOtherMessages,
        chatViewModel::sendMessage,
    )
}

@Composable
fun ChatView(
    importantMessages: List<ChatMessage>,
    allOtherMessages: List<ChatMessage>,
    onMessageSent: (ChatMessage) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
            MessageList(
                modifier = Modifier.fillMaxWidth(0.5f),
                messages = allOtherMessages,
                title = "Messages",
                contentAlignment = Alignment.CenterStart,
            )
            MessageList(
                modifier = Modifier.fillMaxWidth(),
                messages = importantMessages,
                title = "Important messages",
                contentAlignment = Alignment.CenterEnd,
            )
        }
        CreateMessage(onMessageSent = onMessageSent)
    }
}