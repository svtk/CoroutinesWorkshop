package com.kotlinconf.workshop.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureStatusRouting() {
    routing {
        get("/") {
            call.respondText("The server is running")
        }
    }
}