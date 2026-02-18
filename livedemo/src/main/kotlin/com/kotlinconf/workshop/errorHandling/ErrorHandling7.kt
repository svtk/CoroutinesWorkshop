package com.kotlinconf.workshop.errorHandling

import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.seconds

// Handling exception with CoroutineExceptionHandler
suspend fun main() {
    val exceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            println("Caught $throwable")
        }

    val scope = CoroutineScope(SupervisorJob() + exceptionHandler)
    scope.launch { heartbeat() }
    val deferred = scope.async { doWorkAndThrow() }
    scope.launch {
        deferred.await()
    }
    delay(6.seconds)
}


