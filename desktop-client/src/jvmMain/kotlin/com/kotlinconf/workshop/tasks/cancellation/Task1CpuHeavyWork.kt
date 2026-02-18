package com.kotlinconf.workshop.tasks.cancellation

import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.seconds

fun main() = runBlocking {
    val job = launch(Dispatchers.IO) {
        var index = 0
        // initial code:
//        while (true) {
        while (isActive) {
            val result = doCpuHeavyWork(200)
            println("Done (${index++}): $result")
        }
    }

    delay(1.seconds)
    job.cancelAndJoin()
    println("✅ Process finished correctly.")
}

fun doCpuHeavyWork(timeMillis: Int): Int {
    var counter = 0
    val startTime = System.currentTimeMillis()
    while (System.currentTimeMillis() < startTime + timeMillis) {
        counter++
    }
    return counter
}