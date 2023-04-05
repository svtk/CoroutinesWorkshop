package com.kotlinconf.workshop.kettle.network

import com.kotlinconf.workshop.WorkshopServerConfig.HOST
import com.kotlinconf.workshop.WorkshopServerConfig.PORT
import com.kotlinconf.workshop.WorkshopServerConfig.WS_SERVER_URL
import com.kotlinconf.workshop.kettle.CelsiusTemperature
import com.kotlinconf.workshop.kettle.KettlePowerState
import com.kotlinconf.workshop.util.log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

open class NetworkKettleService : KettleService {
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
    private var stableNetwork = true
    private val host = "http://0.0.0.0:9020/"
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

    override suspend fun switchOn(desiredTemperature: CelsiusTemperature) {
        client.post(onEndpoint) {
            contentType(ContentType.Application.Json)
            setBody(desiredTemperature)
        }
    }

    override suspend fun switchOff() {
        client.post(offEndpoint)
    }

    private suspend fun getTemperature(): CelsiusTemperature? {
        val response = client.get(temperatureEndpoint())
        if (!response.status.isSuccess()) {
            log("Network error occurred: ${response.status}")
            return null
        }
        return response.body<CelsiusTemperature>()
            .also { log("Loading temperature: $it Celsius") }
    }

    override fun observeTemperature(): Flow<CelsiusTemperature?> = flow {
        // initial code:
//        emit(getTemperature())
        while (true) {
            delay(1000)
            emit(getTemperature())
        }
    }

    override fun observeKettlePowerState(): Flow<KettlePowerState> = flow {
        // initial code:
//        val socketSession = openWebSocketSession()
//        val kettlePowerState: KettlePowerState = socketSession.receiveDeserialized()
//        log("Received element via websocket: $kettlePowerState")

        val socketSession = openWebSocketSession()
        while (true) {
            val kettlePowerState: KettlePowerState = socketSession.receiveDeserialized()
            log("Received element via websocket: $kettlePowerState")
            emit(kettlePowerState)
        }
    }
}