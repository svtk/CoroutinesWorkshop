package com.kotlinconf.workshop.puzzlers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

fun main() {
    val scope = CoroutineScope(Job())
    scope.launch {
        println("A: Begin")
        delay(2.seconds)
        println("A: End")
    }
    scope.launch {
        println("B: Begin")
        delay(2.seconds)
        println("B: End")
    }
}
