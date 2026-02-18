package com.kotlinconf.workshop.errorHandling

import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Duration.Companion.milliseconds

// We are using coroutineScope, and it crashes
// when one of its children crashes
// heartbeat() is canceled
suspend fun main() {
    coroutineScope {
        launch { heartbeat() }
        launch { doWorkAndThrow() }
    }
}

suspend fun heartbeat() {
    repeat(5) {
        delay(1.seconds)
        println("heartbeat")
    }
}

suspend fun doWorkAndThrow(): Int {
    repeat(2) {
        delay(500.milliseconds)
        println("working")
    }
    throw MyException("BOOM")
}



