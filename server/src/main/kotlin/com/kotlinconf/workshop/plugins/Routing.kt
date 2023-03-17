package com.kotlinconf.workshop.plugins

import com.kotlinconf.workshop.Comment
import com.kotlinconf.workshop.db.DAOFacade
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.delay

fun Application.configureRouting(dao: DAOFacade) {
    routing {
        get("") {
            call.respondText("Hello!")
        }
        get("/issues") {
            delay(500L)
            call.respond(dao.issues)
        }
        get("/issue/{id}/comments") {
            delay(500L)
            call.parameters["id"]?.toLongOrNull()?.let { id ->
                call.respond(dao.getComments(id))
            } ?: run {
                call.respond(listOf<Comment>())
            }
        }
    }
}
