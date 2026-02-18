package com.kotlinconf.workshop

import com.kotlinconf.workshop.util.log
import kotlinx.coroutines.*
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds


class NumbersStation {
    private val scope = CoroutineScope(SupervisorJob())

    // Initially no code below this line
}

suspend fun main() {
    val station = NumbersStation()
    // Initially no code below this
}

suspend fun getNewNumber(): Int {
    val number = Random.nextInt()
    log("Generated $number")
    delay(500.milliseconds)
    return number
}