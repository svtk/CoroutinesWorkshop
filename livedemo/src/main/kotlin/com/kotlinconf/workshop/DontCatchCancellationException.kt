package foo

import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Duration.Companion.milliseconds

suspend fun doSomeWorkThatMayFail() {
    println("I'm working...")
    delay(500.milliseconds)
}

suspend fun main() {
    val cs = CoroutineScope(Dispatchers.Default)
    val myJob = cs.launch {
        while (true) {
            try {
                doSomeWorkThatMayFail()
            } catch (e: Exception) {
                println("Didn't work this time! $e")
            }
        }
    }
    delay(2.seconds)
    println("Enough!")
    myJob.cancelAndJoin()
}