@file:Suppress("DuplicatedCode")
package com.kotlinconf.workshop.flows

import com.kotlinconf.workshop.util.log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Duration.Companion.milliseconds

fun main(): Unit = runBlocking {
    val numbers = flow<Int> {
        var x = 0
        repeat(4) {
            log("Generated $x")
            emit(x++)
            delay(500.milliseconds)
        }
    }
    launch {
        numbers.collect {
            log("A Received $it")
            delay(1.seconds)
        }
    }
    launch {
        numbers.collect {
            log("B Received $it")
        }
    }
}