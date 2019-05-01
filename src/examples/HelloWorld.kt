package examples

import kotlinx.coroutines.*

fun main() = runBlocking {
    launch(Dispatchers.Default) {
        delay(1000L)
        println("World!")
    }
    println("Hello,")
}