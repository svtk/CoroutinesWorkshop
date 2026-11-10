package com.kotlinconf.workshop.plugins

import io.ktor.server.application.*
import io.ktor.server.http.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.request.*
import kotlinx.coroutines.CancellationException
import org.slf4j.event.Level
import kotlin.time.TimeSource

fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    // Cancel the handler coroutine as soon as the client closes the connection
    // (e.g. the desktop client cancels its request), instead of finishing work nobody waits for
    install(HttpRequestLifecycle) {
        cancelCallOnClose = true
    }

    // Make those cancellations clearly visible in the server log
    intercept(ApplicationCallPipeline.Monitoring) {
        val start = TimeSource.Monotonic.markNow()
        try {
            proceed()
        } catch (e: CancellationException) {
            call.application.log.warn(
                "CANCELLED: {} {} after {} - {}",
                call.request.httpMethod.value,
                call.request.uri,
                start.elapsedNow(),
                (e.cause ?: e)::class.simpleName,
            )
            throw e
        }
    }
}
