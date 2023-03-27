package com.kotlinconf.workshop.plugins

import com.kotlinconf.workshop.Comment
import com.kotlinconf.workshop.data.BlogFakeData
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.delay

fun Application.configureBlogRouting() {
    routing {
        get("/articles") {
            call.respond(BlogFakeData.getArticles())
        }
        get("/articles/{id}/comments") {
            call.parameters["id"]?.toIntOrNull()?.let { id ->
                delay(BlogFakeData.getDelay(id))
                call.respond(BlogFakeData.getComments(id))
            } ?: run {
                call.respond(listOf<Comment>())
            }
        }
    }
}
