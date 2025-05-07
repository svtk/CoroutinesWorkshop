package com.kotlinconf.workshop.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

var money = 0

fun Application.configureWalletRouting() {
    routing {
        route("wallet") {
            get("/earn") {
                money++
                call.respond(HttpStatusCode.OK)
            }
            get("/money") {
                call.respondText(money.toString())
            }
        }
    }
}