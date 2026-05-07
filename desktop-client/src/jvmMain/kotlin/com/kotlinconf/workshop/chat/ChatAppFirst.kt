package com.kotlinconf.workshop.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kotlinconf.workshop.chat.network.NetworkChatService
import com.kotlinconf.workshop.chat.ui.SimpleChatView
import com.kotlinconf.workshop.chat.ui.SimpleChatViewModel

@Composable
fun ChatAppFirst(simpleChatViewModel: SimpleChatViewModel) {
    MaterialTheme {
        Box(Modifier.fillMaxSize().padding(10.dp)) {
            SimpleChatView(simpleChatViewModel)
        }
    }
}

@Composable
fun BasicChatWindow(
    onCloseRequest: () -> Unit,
    verifyServerOnLaunch: Boolean,
    title: String = "Basic Chat",
) {
    val chatService = remember { NetworkChatService() }
    if (verifyServerOnLaunch) {
        LaunchedEffect(chatService) {
            chatService.ensureServerIsRunning()
        }
    }
    Window(
        onCloseRequest = onCloseRequest,
        title = title,
        state = rememberWindowState(width = 500.dp, height = 300.dp),
    ) {
        val chatViewModel = viewModel { SimpleChatViewModel(chatService) }
        ChatAppFirst(chatViewModel)
    }
}

fun main() = application {
    BasicChatWindow(
        onCloseRequest = ::exitApplication,
        verifyServerOnLaunch = true,
    )
}
