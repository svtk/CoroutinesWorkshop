package com.kotlinconf.workshop.tasks.cancellation

import kotlinx.coroutines.*

fun main() = runBlocking {
    val job = launch(Dispatchers.Default) {
        var index = 0
        while (true) {
            val result = doCpuHeavyWork(200)
            println("Done (${index++}): $result")
        }
    }

    delay(1000)
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