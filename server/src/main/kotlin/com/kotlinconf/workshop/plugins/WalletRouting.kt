package com.kotlinconf.workshop.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

var money = 0

fun Application.configureWalletRouting() {
    routing {
        route("wallet") {
            get("/earn") {
                money++
            }
            get("/money") {
                call.respondText(money.toString())
            }
        }
    }
}