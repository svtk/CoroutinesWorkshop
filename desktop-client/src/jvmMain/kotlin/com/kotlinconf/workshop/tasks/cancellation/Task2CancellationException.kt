package com.kotlinconf.workshop.tasks.cancellation

import kotlinx.coroutines.*
import kotlin.random.Random

fun main() = runBlocking {
    val myJob = launch {
        while (true) {
            try {
                doSomeWorkThatMayFail()
            // initial code:
//            } catch (e: Exception) {
            } catch (e: MyException) {
                println("Oops! ${e.message}")
            }
        }
    }
    delay(2000)
    println("Enough!")
    myJob.cancelAndJoin()
    println("✅ Process finished correctly.")
}

suspend fun doSomeWorkThatMayFail() {
    delay(300)
    if (Random.nextBoolean()) {
        println("I'm working...")
    } else {
        throw MyException("Didn't work this time!")
    }
}

class MyException(message: String): Exception(message)