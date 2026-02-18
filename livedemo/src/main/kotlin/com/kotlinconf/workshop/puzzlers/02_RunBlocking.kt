@file:Suppress("DuplicatedCode")

package com.kotlinconf.workshop.puzzlers

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

suspend fun main(): Unit = runBlocking {
    launch {
        println("A: Begin")
        delay(2.seconds)
        println("A: End")
    }
    launch {
        println("B: Begin")
        delay(2.seconds)
        println("B: End")
    }
}