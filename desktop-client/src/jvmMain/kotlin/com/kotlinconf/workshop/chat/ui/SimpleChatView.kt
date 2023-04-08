package com.kotlinconf.workshop.chat.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SimpleChatView(simpleChatViewModel: SimpleChatViewModel) {
    val messages = simpleChatViewModel.messages.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        MessageList(
            modifier = Modifier.fillMaxSize().weight(1f),
            messages = messages.value,
            title = "Messages",
            contentAlignment = Alignment.CenterStart,
        )
        CreateMessage(onMessageSent = { simpleChatViewModel.sendMessage(it) })
    }
}