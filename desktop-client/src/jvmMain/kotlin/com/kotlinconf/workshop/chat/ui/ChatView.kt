package com.kotlinconf.workshop.chat.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kotlinconf.workshop.ChatMessage

@Composable
fun ChatView(chatViewModel: ChatViewModel) {
    val importantMessages = chatViewModel.importantMessages.collectAsState()
    val allOtherMessages = chatViewModel.allOtherMessages.collectAsState()
    var textFieldState by remember { mutableStateOf("") }
    Column {
        Row {
            MessageList(importantMessages.value)
            MessageList(allOtherMessages.value)
        }
        Row {
            TextField(
                value = textFieldState,
                onValueChange = { textFieldState = it }
            )
            Button(onClick = {
                chatViewModel.sendMessage(ChatMessage(textFieldState))
                textFieldState = ""
            }) {
                Text(text = "Send")
            }
        }
    }

}

@Composable
fun MessageList(chatMessages: List<ChatMessage>) {
    LazyColumn(Modifier.width(200.dp)) {
        items(chatMessages) { message ->
            Message(message)
        }
    }
}

@Composable
fun Message(chatMessage: ChatMessage) {
    Text(text = chatMessage.content)
}