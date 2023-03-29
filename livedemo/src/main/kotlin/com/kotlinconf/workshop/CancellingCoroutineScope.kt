package com.kotlinconf.workshop

import kotlinx.coroutines.*

suspend fun main() = runBlocking {
    val myScope = CoroutineScope(coroutineContext + Dispatchers.IO)

    myScope.launch {
        var x = 0
        while (true) {
            println("A: ${x++}")
            delay(200)
        }
    }

    myScope.launch {
        var y = 0
        while (true) {
            y += 2
            println("B: $y")
            doCpuHeavyWork(200)
            // Cancellation is cooperative!
            // try: Yield, ensureActive, isActive
        }
    }

    delay(2000)
    myScope.cancel()
}

fun doCpuHeavyWork(timeMillis: Int) {
    Thread.sleep(timeMillis.toLong())
}