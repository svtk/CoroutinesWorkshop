package com.kotlinconf.workshop.logging

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.filter.Filter
import ch.qos.logback.core.spi.FilterReply

/**
 * Drops the `ERROR Unhandled server error: "Call context was cancelled by `HttpRequestLifecycle` plugin"`
 * entry (with full stack trace) that Ktor's engine logs whenever a client disconnects mid-request.
 *
 * The cancellation is intentional and already logged as a concise `CANCELLED: ...` warning
 * in `configureMonitoring()`, so the stack trace is just noise. All other errors pass through.
 */
class HttpRequestLifecycleCancellationFilter : Filter<ILoggingEvent>() {
    override fun decide(event: ILoggingEvent): FilterReply {
        val throwable = event.throwableProxy ?: return FilterReply.NEUTRAL
        val isLifecycleCancellation = event.level == Level.ERROR &&
            throwable.className == "java.util.concurrent.CancellationException" &&
            throwable.message?.contains("cancelled by `HttpRequestLifecycle` plugin") == true
        return if (isLifecycleCancellation) FilterReply.DENY else FilterReply.NEUTRAL
    }
}
