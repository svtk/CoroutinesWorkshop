package com.kotlinconf.workshop

import com.kotlinconf.workshop.plugins.configureMonitoring
import com.kotlinconf.workshop.plugins.configureRouting
import com.kotlinconf.workshop.plugins.configureSerialization
import com.kotlinconf.workshop.plugins.configureSockets
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 9010, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureMonitoring()
    configureSerialization()
    val issueTracker = setupIssueTracker()
    configureRouting(issueTracker)
    configureSockets(issueTracker)
}
