package com.kotlinconf.workshop

import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Duration.Companion.milliseconds


suspend fun main() {
    coroutineScope {
        launch {
            while (true) {
                println("X")
                delay(500.milliseconds)
            }
        }
        launch {
            while (true) {
                println("Y")
                delay(500.milliseconds)
            }
        }
        delay(2.seconds)
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
            delay(200.milliseconds)
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

    delay(2.seconds)
    myScope.cancel()
}

fun doCpuHeavyWork(timeMillis: Int) {
    Thread.sleep(timeMillis.toLong())
}