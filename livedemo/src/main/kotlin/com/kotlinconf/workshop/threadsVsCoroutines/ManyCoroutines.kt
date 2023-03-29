package com.kotlinconf.workshop.threadsVsCoroutines

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun main() = coroutineScope {
    val jobs = List(100_000) {
        launch {
            delay(1000)
            print(".")
        }
    }
    jobs.forEach { it.join() }
}