package com.kotlinconf.workshop.demoVersion

import com.kotlinconf.workshop.util.log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Duration.Companion.milliseconds

class NumbersStationDemoVersion {
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

fun main() = runBlocking<Unit> {
    val station = NumbersStationDemoVersion()
    // Initially no code below this
    station.beginBroadcasting()
    // Nobody listening!
    delay(5.seconds)
    launch {
        station.numbers.collect {
            log("Received $it")
        }
    }
}

suspend fun getNewNumber(): Int {
    val number = Random.nextInt()
    log("Generated $number")
    delay(500.milliseconds)
    return number
}