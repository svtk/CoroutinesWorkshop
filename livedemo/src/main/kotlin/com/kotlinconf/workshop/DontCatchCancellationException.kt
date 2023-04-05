package foo

import kotlinx.coroutines.*

suspend fun doSomeWorkThatMayFail() {
    println("I'm working...")
    delay(500)
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
    delay(2000)
    println("Enough!")
    myJob.cancelAndJoin()
}