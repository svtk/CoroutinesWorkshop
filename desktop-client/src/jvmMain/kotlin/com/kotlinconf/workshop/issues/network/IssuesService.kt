package com.kotlinconf.workshop.issues.network

import com.kotlinconf.workshop.IssueEvent
import com.kotlinconf.workshop.WorkshopServerConfig
import com.kotlinconf.workshop.util.log
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

class IssuesService {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
    }

    private suspend fun openWebSocketSession(): DefaultClientWebSocketSession {
        return client.webSocketSession(
            method = HttpMethod.Get,
            host = WorkshopServerConfig.HOST,
            port = WorkshopServerConfig.PORT,
            path = "/issueEvents"
        ).also {
            log("Opening a web socket session for ${WorkshopServerConfig.WS_SERVER_URL}/issueEvents")
        }
    }

    fun getEvents(): Flow<IssueEvent> = flow {
        val ws = openWebSocketSession()
        while (true) {
            emit(ws.receiveDeserialized<IssueEvent>())
        }
    }
}