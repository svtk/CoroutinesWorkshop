package com.kotlinconf.workshop

import kotlinx.coroutines.*


suspend fun main() {
    coroutineScope {
        launch {
            while (true) {
                println("X")
                delay(500)
            }
        }
        launch {
            while (true) {
                println("Y")
                delay(500)
            }
        }
        delay(2000)
        cancel()
    }
}

// Alternative implementation that demonstrates coroutine scope cancellation is cooperative when using the
// CoroutineScope function


suspend fun mainX() = runBlocking {
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