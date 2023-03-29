package com.kotlinconf.workshop.plugins

import com.kotlinconf.workshop.data.Kettle
import com.kotlinconf.workshop.kettle.CelsiusTemperature
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlin.random.Random

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
            val failureProbability = call.request.queryParameters["failure"]?.toDouble() ?: 0.0
            if (Random.nextDouble() < failureProbability) {
                return@get call.respond(HttpStatusCode.InternalServerError)
            }
            call.respond(kettle.getTemperature())
        }
    }
}

fun Application.configureKettleSockets(kettle: Kettle) {
    routing {
        webSocket("/kettle-ws") {
            kettle.observeKettleState().collect { state ->
                sendSerialized(state)
            }
        }
    }
}
