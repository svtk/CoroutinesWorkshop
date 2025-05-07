package com.kotlinconf.workshop.plugins

import com.kotlinconf.workshop.ChatMessage
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

fun Application.configureChatRouting(chat: Chat = Chat()) {
    routing {
        webSocket("/chat") {
            handleSocket(chat)
        }
    }
}

// Task: Implement chat server using SharedFlow
suspend fun WebSocketServerSession.handleSocket(chat: Chat) {
    launch {
        // use sendSerialized(message) to send a message to the WebSocket
        chat.messageFlow.collect {
            sendSerialized(it)
        }
    }

    // use receiveDeserialized<ChatMessage>() to receive a message from the WebSocket
    while (isActive) {
        val message = receiveDeserialized<ChatMessage>()
        chat.broadcastMessage(message)
    }
}

class Chat {
    private val _messageFlow: MutableSharedFlow<ChatMessage> = MutableSharedFlow()
    val messageFlow: SharedFlow<ChatMessage> get() = _messageFlow
    suspend fun broadcastMessage(message: ChatMessage) {
        _messageFlow.emit(message)
    }
}