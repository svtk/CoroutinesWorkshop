package com.kotlinconf.workshop.tasks.cancellation

import kotlinx.coroutines.*

fun main() = runBlocking {
    val job = launch(Dispatchers.IO) {
        var y = 0
        // initial code:
//        while (true) {
        while (isActive) {
            y += 2
            println("A: $y")
            doCpuHeavyWork(200)
        }
    }

    delay(1000)
    job.cancelAndJoin()
}

fun doCpuHeavyWork(timeMillis: Int) {
    Thread.sleep(timeMillis.toLong())
}