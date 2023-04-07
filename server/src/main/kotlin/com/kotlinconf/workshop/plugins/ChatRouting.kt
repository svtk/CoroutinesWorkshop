package com.kotlinconf.workshop.plugins

import com.kotlinconf.workshop.ChatMessage
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

fun Application.configureChatRouting(chat: Chat = Chat()) {
    routing {
        webSocket("/chat") { // websocketSession
            val sess = this
            coroutineScope {
                launch {
                    while (true) {
                        val message = sess.receiveDeserialized<ChatMessage>()
                        chat.sendMessage(message)
                    }
                }
                launch {
                    chat.messageFlow.collect {
                        sess.sendSerialized(it)
                    }
                }
            }
        }
    }
}

class Chat {
    private val _messageFlow: MutableSharedFlow<ChatMessage> = MutableSharedFlow()
    val messageFlow: MutableSharedFlow<ChatMessage> get() = _messageFlow
    suspend fun sendMessage(message: ChatMessage) {
        _messageFlow.emit(message)
    }
}