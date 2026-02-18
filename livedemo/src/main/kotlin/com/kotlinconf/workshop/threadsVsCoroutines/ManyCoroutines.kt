package com.kotlinconf.workshop.threadsVsCoroutines

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

suspend fun main() = coroutineScope {
    val jobs = List(100_000) {
        launch {
            delay(1.seconds)
            print(".")
        }
    }
    jobs.forEach { it.join() }
}