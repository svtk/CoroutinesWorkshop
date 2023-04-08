package com.kotlinconf.workshop

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlin.random.Random


class NumbersStation {
    private val scope = CoroutineScope(SupervisorJob())

    // Initially no code below this line
    private val _numbers: MutableSharedFlow<Int> = MutableSharedFlow(50)
    val numbers: SharedFlow<Int> get() = _numbers

    fun beginBroadcasting() {
        scope.launch {
            while (true) {
                _numbers.emit(getNewNumber())
            }
        }
    }
}

suspend fun main() {
    val station = NumbersStation()
    // Initially no code below this
    station.beginBroadcasting()
    // Nobody listening!
    delay(5000)
    coroutineScope {
        launch {
            station.numbers.collect {
                println("Received $it")
            }
        }
    }
}


suspend fun getNewNumber(): Int {
    val number = Random.nextInt()
    println("Generated $number")
    delay(500)
    return number
}