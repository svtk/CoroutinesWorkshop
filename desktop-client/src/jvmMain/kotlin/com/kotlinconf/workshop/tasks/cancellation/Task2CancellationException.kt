package com.kotlinconf.workshop.tasks.cancellation

import kotlinx.coroutines.*
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Duration.Companion.milliseconds

fun main() = runBlocking {
    val myJob = launch {
        while (true) {
            try {
                doSomeWorkThatMayFail()
            } catch (e: Exception) {
                println("Oops! ${e.message}")
            }
        }
    }
    delay(2.seconds)
    println("Enough!")
    myJob.cancelAndJoin()
    println("✅ Process finished correctly.")
}

suspend fun doSomeWorkThatMayFail() {
    delay(300.milliseconds)
    if (Random.nextBoolean()) {
        println("I'm working...")
    } else {
        throw MyException("Didn't work this time!")
    }
}

class MyException(message: String): Exception(message)