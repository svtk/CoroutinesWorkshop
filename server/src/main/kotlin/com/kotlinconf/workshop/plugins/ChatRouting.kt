package com.kotlinconf.workshop.plugins

import com.kotlinconf.workshop.ChatMessage
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

fun Application.configureChatRouting(chat: Chat = Chat()) {
    routing {
        webSocket("/chat") {
            handleSocket(this, chat)
        }
    }
}

// Task: Implement chat server using SharedFlow
suspend fun handleSocket(
    socket: WebSocketServerSession,
    chat: Chat
) {
    coroutineScope {
        launch {
            // use socket.receiveDeserialized<ChatMessage>() to receive a message from the WebSocket
        }
        launch {
            // use socket.sendSerialized(message) to send a message to the WebSocket
        }
    }
}

class Chat {
    val messageFlow: SharedFlow<ChatMessage>
        field = MutableSharedFlow()

    suspend fun broadcastMessage(message: ChatMessage) {
        messageFlow.emit(message)
    }
}
