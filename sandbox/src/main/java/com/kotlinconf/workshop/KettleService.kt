package com.kotlinconf.workshop

import com.kotlinconf.workshop.kettle.CelsiusTemperature
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class KettleService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    private val host = "http://0.0.0.0:9020/"
    private val onEndpoint = "$host/kettle/on"
    private val offEndpoint = "$host/kettle/off"
    private val temperatureEndpoint = "$host/kettle/temperature"

    suspend fun switchOn(desiredTemperature: CelsiusTemperature) {
        client.post(onEndpoint) {
            contentType(ContentType.Application.Json)
            setBody(desiredTemperature)
        }
    }

    suspend fun switchOff() {
        client.post(offEndpoint)
    }

    suspend fun getTemperature(): CelsiusTemperature {
        return client.get(temperatureEndpoint).body()
    }
}