package com.kotlinconf.workshop.cancellation

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

suspend fun performLongComputation(): Int {
    delay(500.milliseconds)
    return 5
}

suspend fun main() {
    coroutineScope {
        val computation = async { performLongComputation() }
        val result = computation.await()
        computation.cancel()
        val result2 = computation.await()
    }
}