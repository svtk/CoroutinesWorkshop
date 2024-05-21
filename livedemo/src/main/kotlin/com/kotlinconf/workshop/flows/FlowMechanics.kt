@file:Suppress("DuplicatedCode")
package com.kotlinconf.workshop.flows

import com.kotlinconf.workshop.util.log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow

fun main(): Unit = runBlocking {
    val numbers = flow<Int> {
        var x = 0
        repeat(4) {
            log("Generated $x")
            emit(x++)
            delay(500)
        }
    }
    launch {
        numbers.collect {
            log("A Received $it")
            delay(1000)
        }
    }
    launch {
        numbers.collect {
            log("B Received $it")
        }
    }
}