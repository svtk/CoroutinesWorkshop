package com.kotlinconf.workshop.plugins

import com.kotlinconf.workshop.Kettle
import com.kotlinconf.workshop.kettle.Temperature
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureKettleRouting(kettle: Kettle) {
    routing {
        post("kettle/on") {
            val desiredTemperature = call.receive<Temperature>()
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