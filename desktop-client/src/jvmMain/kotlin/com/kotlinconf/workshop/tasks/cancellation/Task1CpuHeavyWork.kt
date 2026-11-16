package com.kotlinconf.workshop.tasks.cancellation

import kotlinx.coroutines.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

suspend fun main() {
    coroutineScope {
        val job = launch(Dispatchers.Default) {
            var index = 0
            while (true) {
                val result = doCpuHeavyWork(200.milliseconds)
                println("Done (${index++}): $result")
            }
        }

        delay(1.seconds)
        job.cancelAndJoin()
        println("✅ Process finished correctly.")
    }
}

fun doCpuHeavyWork(duration: Duration): Int {
    var counter = 0
    val startTime = TimeSource.Monotonic.markNow()
    while (startTime.elapsedNow() < duration) {
        counter++
    }
    return counter
}