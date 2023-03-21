package com.kotlinconf.workshop.plugins

import com.kotlinconf.workshop.Comment
import com.kotlinconf.workshop.IssueCreateRequest
import com.kotlinconf.workshop.IssueId
import com.kotlinconf.workshop.IssueTracker
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

fun Application.configureRouting(issueTracker: IssueTracker) {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("issues") {
            // Endpoint to simulate single slow request
            get {
                delay(2.seconds)
                call.respond(issueTracker.allIssues())
            }
            post {
                val issueReq = call.receive<IssueCreateRequest>()
                val newIssue = issueTracker.addIssue(issueReq.author, issueReq.title)
                call.respond(newIssue)
            }
            // Endpoint to simulate that parallel requests are faster than sequential
            // Endpoint to simulate failure states, possibility of retrying operations
            route("{id}") {
                // ?failure=0.3
                route("comments") {
                    get {
                        val intId =
                            call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
                        val failureProbability = call.request.queryParameters["failure"]?.toDouble() ?: 0.0
                        if (Random.nextDouble() < failureProbability) {
                            return@get call.respond(HttpStatusCode.InternalServerError)
                        }
                        delay(500)
                        call.respond(issueTracker.commentsForId(IssueId(intId)))
                    }
                    post {
                        val intId =
                            call.parameters["id"]?.toIntOrNull() ?: return@post call.respond(HttpStatusCode.BadRequest)
                        val comment = call.receive<Comment>()
                        issueTracker.addComment(IssueId(intId), comment)
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }
        }
    }
}
