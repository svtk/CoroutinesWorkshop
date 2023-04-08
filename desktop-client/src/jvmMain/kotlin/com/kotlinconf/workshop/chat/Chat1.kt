package com.kotlinconf.workshop.chat

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.kotlinconf.workshop.chat.network.NetworkChatService
import com.kotlinconf.workshop.chat.ui.SimpleChatView
import com.kotlinconf.workshop.chat.ui.SimpleChatViewModel

@Composable
@Preview
fun App(simpleChatViewModel: SimpleChatViewModel) {
    MaterialTheme {
        Box(Modifier.fillMaxSize().padding(10.dp)) {
            SimpleChatView(simpleChatViewModel)
        }
    }
}

fun main() = application {
    val chatViewModel = remember { SimpleChatViewModel(NetworkChatService()) }
    Window(
        onCloseRequest = {
            exitApplication()
        },
        title = "Simple chat example",
        state = rememberWindowState(width = 500.dp, height = 300.dp),
    ) {
        App(chatViewModel)
    }
}