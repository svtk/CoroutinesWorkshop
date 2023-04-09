package com.kotlinconf.workshop.errorHandling

import kotlinx.coroutines.*

// Handling exception in try-catch
suspend fun main() {
    supervisorScope {
        launch { heartbeat() }
        val deferred = async { doWorkAndThrow() }
        try {
            deferred.await()
        } catch (e: MyException) {
            handleException(e)
        }
    }
}
