package com.kotlinconf.workshop.coroutineBuilders

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

suspend fun main() {
    var x = AtomicInteger(0)
    coroutineScope {
        repeat(10_000) {
            launch {
                x.incrementAndGet()
            }
        }
    }
    println(x)
}