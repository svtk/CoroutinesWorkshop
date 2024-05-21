package com.kotlinconf.workshop

import com.kotlinconf.workshop.util.log
import kotlinx.coroutines.*
import kotlin.random.Random


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
    delay(500)
    return number
}