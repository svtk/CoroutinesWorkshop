package com.kotlinconf.workshop.coroutineBuilders.structured

import com.kotlinconf.workshop.util.log
import kotlinx.coroutines.*

// Option+Shift
// GlobalScope -- no longer waits
// Dispatcher
// CoroutineName("my")

fun main0() = runBlocking {
    val parentJob = launch {
        log("Parent starts")
        val firstChildJob = launch {
            log("First child starts")
            delay(1000)
            log("First child completes")
        }
        val secondChildJob = launch {
            log("Second child starts")
            delay(1500)
            log("Second child completes")
        }
        log("Parent completes")
    }
    delay(500)
    log("I've changed my mind")
    parentJob.cancel()
    delay(2000)
}

fun main1() = runBlocking {
    val parentJob = launch {
        log("Parent starts")
        val firstChildJob = GlobalScope.launch {
            log("First child starts")
            delay(1000)
            log("First child completes")
        }
        val secondChildJob = GlobalScope.launch {
            log("Second child starts")
            delay(1500)
            log("Second child completes")
        }
        log("Parent completes")
    }
    delay(500)
    log("I've changed my mind")
    parentJob.cancel()
    delay(2000)
}

// Remove GlobalScope
// Inheriting the Dispatchers!
// Inheriting the Context
fun main() = runBlocking {
    val context = Dispatchers.Default + CoroutineName("my")
    val parentJob = launch(context) {
        log("Parent starts")
        val firstChildJob = launch {
            log("First child starts")
            delay(1000)
            log("First child completes")
        }
        val secondChildJob = launch {
            log("Second child starts")
            delay(1500)
            log("Second child completes")
        }
        log("Parent completes")
    }
    delay(500)
    log("I've changed my mind")
    parentJob.cancel()
    delay(2000)
}