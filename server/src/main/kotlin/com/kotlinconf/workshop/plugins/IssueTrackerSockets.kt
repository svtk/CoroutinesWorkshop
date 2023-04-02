package com.kotlinconf.workshop.plugins

import com.kotlinconf.workshop.GlobalVirtualUser
import com.kotlinconf.workshop.IssueTracker
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

fun Application.configureIssueTrackerEventSockets(issueTracker: IssueTracker) {
    routing {
        webSocket("/issueEvents") { // websocketSession
            GlobalVirtualUser.instance.beginPosting(issueTracker)
            issueTracker.issueEvents.onEach {
                sendSerialized(it)
            }.collect()
        }
    }
}