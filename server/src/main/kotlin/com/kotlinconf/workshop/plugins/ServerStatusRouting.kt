package com.kotlinconf.workshop.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureStatusRouting() {
    routing {
        get("/") {
            call.respondText(
                """
                The server is running. Here's its capabilities:
                - GET /articles
                - GET /articles/{id}/comments
                - GET /articles/{id}/comments?failure=0.3
                - GET /wallet/earn
                - GET /wallet/money
                - POST /kettle/on
                - POST /kettle/off
                - GET /kettle/temperature
                - GET /kettle/temperature?failure=0.3
                - WS /kettle-ws
                - WS /chat
                """.trimIndent()
            )
        }
    }
}
