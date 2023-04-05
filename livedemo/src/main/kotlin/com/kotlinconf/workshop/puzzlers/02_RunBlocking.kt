@file:Suppress("DuplicatedCode")

package com.kotlinconf.workshop.puzzlers

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

suspend fun main(): Unit = runBlocking {
    launch {
        println("A: Begin")
        delay(2000)
        println("A: End")
    }
    launch {
        println("B: Begin")
        delay(2000)
        println("B: End")
    }
}