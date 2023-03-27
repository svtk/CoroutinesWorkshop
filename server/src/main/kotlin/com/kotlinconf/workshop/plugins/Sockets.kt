package com.kotlinconf.workshop.plugins

import com.kotlinconf.workshop.IssueTracker
import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.json.Json
import java.time.Duration

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }
}

fun Application.configureEventsSockets(issueTracker: IssueTracker) {
    routing {
        webSocket("/issueEvents") { // websocketSession
            issueTracker.issueEvents.onEach {
                sendSerialized(it)
            }.collect()
        }
    }
}
