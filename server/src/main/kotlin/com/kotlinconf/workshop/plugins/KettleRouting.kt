package com.kotlinconf.workshop.plugins

import com.kotlinconf.workshop.Kettle
import com.kotlinconf.workshop.kettle.CelsiusTemperature
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.delay

fun Application.configureKettleRouting(kettle: Kettle) {
    routing {
        post("kettle/on") {
            val desiredTemperature = call.receive<CelsiusTemperature>()
            kettle.switchOn(desired = desiredTemperature)
        }
        post("kettle/off") {
            kettle.switchOff()
        }
        get("kettle/temperature") {
            call.respond(kettle.getTemperature())
        }
    }
}

fun Application.configureKettleSockets(kettle: Kettle) {
    routing {
        webSocket("/kettle-ws") {
            var prevTemperature: CelsiusTemperature? = null
            while (true) {
                val temperature = kettle.getTemperature()
                if (temperature != prevTemperature) {
                    sendSerialized(temperature)
                    prevTemperature = temperature
                }
                delay(1000)
            }
        }
    }
}
