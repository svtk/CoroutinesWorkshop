package com.kotlinconf.workshop.flows

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

// Finite flow
// Collect once
// Collect twice
// Infinite flow

suspend fun main(): Unit = coroutineScope {
    val numbers = flow<Int> {
        var x = 0
        while (true) {
            println("Generated $x")
            emit(x++)
            delay(500)
        }
    }
    launch {
        numbers.collect {
            println("A Received $it")
            delay(2000)
        }
    }
    launch {
        numbers.collect {
            println("B Received $it")
        }
    }
}