package com.kotlinconf.workshop

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

val counterFlow = flow {
    var x = 0
    while (true) {
        emit(x++)
        delay(200.milliseconds)
    }
}

suspend fun main() {
    coroutineScope {
        // A breaks after 10 iterations
        launch(CoroutineName("A")) {
            counterFlow.collect {
                println("A: $it")
                if (it >= 10) error("I've had enough!")
            }
        }
        // B is supposed to stay running!
        launch(CoroutineName("B")) {
            counterFlow.collect {
                println("B: $it")
            }
        }
    }
}