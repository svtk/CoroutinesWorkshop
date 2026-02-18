package com.kotlinconf.workshop.puzzlers

import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.milliseconds

// withTimeout throws TimeoutCancellationException, a subclass of CancellationException.
// https://sebi.io/posts/2023-11-24-withtimeout-doesnt-do-what-you-think-it-does/

class TheProblemOfWithTimeout {
    val cs = CoroutineScope(Job())

    fun start() {
        cs.launch {
            launch {
                while (true) {
                    delay(600.milliseconds)
                    println("Heartbeat")
                }
            }
            withTimeout(500) {
                println("I'm working")
                delay(600.milliseconds)
                println("I did some work!")
            }
            println("I concluded my work!")
        }
    }
}

fun main() {
    TheProblemOfWithTimeout().start()
    Thread.sleep(6000)
}

suspend fun timeoutFunction() {

    try {
        withTimeout(500) {
            performSlowOperation()
        }
    } catch (t: TimeoutCancellationException) {
        t.printStackTrace()
    }


}


suspend fun performSlowOperation() {

}
