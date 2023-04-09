package com.kotlinconf.workshop.network

import com.kotlinconf.workshop.WorkshopServerConfig.WORKSHOP_SERVER_URL
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.net.ConnectException

open class WorkshopKtorService(configureWebsockets: Boolean = false) {
    protected val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        if (configureWebsockets) {
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(Json)
            }
        }
    }

    suspend fun ensureServerIsRunning() {
        fun fail(): Nothing =
            throw ServerSetupException("Workshop server is not available. Please start server!")
        val response = try {
            client.get(WORKSHOP_SERVER_URL)
        } catch (e: ConnectException) {
            fail()
        }
        if (!response.status.isSuccess()) {
            fail()
        }
    }
}

class ServerSetupException(message: String): Exception(message)