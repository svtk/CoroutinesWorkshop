package com.kotlinconf.workshop.errorHandling

import kotlinx.coroutines.*

// It crashes because launch returns immediately!
// Surrounding launch in try-catch doesn’t make sense!
suspend fun main() {
    coroutineScope {
        launch { heartbeat() }
        try {
            launch { doWorkAndThrow() }
        } catch (e: MyException) {
            handleException(e)
        }
    }
}


