package com.kotlinconf.workshop.coroutineBuilders.scope

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds


fun main0() {
    GlobalScope.launch {
        delay(200.milliseconds)
        println("A")
    }
    GlobalScope.launch {
        delay(100.milliseconds)
        println("B")
    }
    println("Here")
}
suspend fun main1() = coroutineScope {
    launch {
        delay(200.milliseconds)
        println("A")
    }
    launch {
        delay(100.milliseconds)
        println("B")
    }
    println("Here")
}