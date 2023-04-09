package com.kotlinconf.workshop.chat.network

import com.kotlinconf.workshop.ChatMessage
import com.kotlinconf.workshop.WorkshopServerConfig
import com.kotlinconf.workshop.network.WorkshopKtorService
import com.kotlinconf.workshop.util.log
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NetworkChatService : ChatService, WorkshopKtorService(configureWebsockets = true) {
    private suspend fun openWebSocketSession(): DefaultClientWebSocketSession {
        return client.webSocketSession(
            method = HttpMethod.Get,
            host = WorkshopServerConfig.HOST,
            port = WorkshopServerConfig.PORT,
            path = "/chat"
        ).also {
            log("Opening a web socket session for ${WorkshopServerConfig.WS_SERVER_URL}/chat")
        }
    }

    override suspend fun sendMessage(chatMessage: ChatMessage) {
        myWsSession?.sendSerialized(chatMessage)
    }

    private var myWsSession: DefaultClientWebSocketSession? = null

    override fun observeMessageEvents(): Flow<ChatMessage> = flow {
        val ws = openWebSocketSession()
        myWsSession = ws
        while (true) {
            emit(ws.receiveDeserialized<ChatMessage>())
        }
    }
}