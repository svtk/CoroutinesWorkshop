package com.kotlinconf.workshop

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
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
    println("Generated $number")
    delay(500)
    return number
}