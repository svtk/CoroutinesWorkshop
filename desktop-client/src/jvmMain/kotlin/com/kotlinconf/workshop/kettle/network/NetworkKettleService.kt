package com.kotlinconf.workshop.kettle.network

import com.kotlinconf.workshop.WorkshopServerConfig.HOST
import com.kotlinconf.workshop.WorkshopServerConfig.PORT
import com.kotlinconf.workshop.WorkshopServerConfig.WS_SERVER_URL
import com.kotlinconf.workshop.kettle.CelsiusTemperature
import com.kotlinconf.workshop.kettle.KettlePowerState
import com.kotlinconf.workshop.network.WorkshopKtorService
import com.kotlinconf.workshop.util.log
import io.ktor.client.call.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NetworkKettleService : KettleService, WorkshopKtorService(configureWebsockets = true) {
    private var stableNetwork = true
    private val host = "http://127.0.0.1:9020/"
    private val onEndpoint = "$host/kettle/on"
    private val offEndpoint = "$host/kettle/off"
    private fun temperatureEndpoint(): String {
        val stableEndpoint = "$host/kettle/temperature"
        return if (stableNetwork) stableEndpoint
        else "$stableEndpoint?failure=0.3"
    }

    private suspend fun openWebSocketSession(): DefaultClientWebSocketSession {
        return client.webSocketSession(
            method = HttpMethod.Get,
            host = HOST,
            port = PORT,
            path = "/kettle-ws"
        ).also {
            log("Opening a web socket session for $WS_SERVER_URL/kettle-ws")
        }
    }

    override suspend fun switchOn() {
        client.post(onEndpoint)
    }

    override suspend fun switchOff() {
        client.post(offEndpoint)
    }

    private suspend fun getTemperature(): CelsiusTemperature {
        val response = client.get(temperatureEndpoint())
        return response.body<CelsiusTemperature>()
            .also { log("Loading temperature: $it Celsius") }
    }

    // Task. Create a flow that emits the kettle temperature every second
    override fun observeTemperature(): Flow<CelsiusTemperature> = flow {
        emit(getTemperature())
    }

    // Task. Create a flow that emits the Kettle power state whenever it receives a WebSocket message
    override fun observeKettlePowerState(): Flow<KettlePowerState> = flow {
        val socketSession = openWebSocketSession()
        val kettlePowerState: KettlePowerState = socketSession.receiveDeserialized()
        log("Received element via websocket: $kettlePowerState")
    }
}