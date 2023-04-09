package com.kotlinconf.workshop.errorHandling

import kotlinx.coroutines.*

// We are using supervisorScope, and it keeps running
// when one of its children crashes
// heartbeat() is still running
suspend fun main() {
    supervisorScope {
        launch { heartbeat() }
        launch { doWorkAndThrow() }
    }
}