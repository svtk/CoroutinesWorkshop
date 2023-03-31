package com.kotlinconf.workshop.plugins

import com.kotlinconf.workshop.Comment
import com.kotlinconf.workshop.data.BlogFakeData
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.delay
import kotlin.random.Random

fun Application.configureBlogRouting() {
    routing {
        get("/articles") {
            call.respond(BlogFakeData.getArticles())
        }
        get("/articles/{id}/comments") {
            call.parameters["id"]?.toIntOrNull()?.let { id ->
                val failureProbability = call.request.queryParameters["failure"]?.toDouble() ?: 0.0
                if (Random.nextDouble() < failureProbability) {
                    return@get call.respond(HttpStatusCode.InternalServerError)
                }
                delay(BlogFakeData.getDelay(id))
                call.respond(BlogFakeData.getComments(id))
            } ?: run {
                call.respond(listOf<Comment>())
            }
        }
    }
}
