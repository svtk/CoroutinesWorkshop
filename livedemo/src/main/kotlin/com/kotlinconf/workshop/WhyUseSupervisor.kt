package com.kotlinconf.workshop

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

val counterFlow = flow {
    var x = 0
    while (true) {
        emit(x++)
        delay(200.milliseconds)
    }
}

suspend fun main() {
    // original code: coroutineScope {
    val cs = CoroutineScope(SupervisorJob())
    // A breaks after 10 iterations
    cs.launch(CoroutineName("A")) {
        counterFlow.collect {
            println("A: $it")
            if (it >= 10) error("I've had enough!")
        }
    }
    // B is supposed to stay running!
    cs.launch(CoroutineName("B")) {
        counterFlow.collect {
            println("B: $it")
        }
    }
    delay(10.seconds)
}