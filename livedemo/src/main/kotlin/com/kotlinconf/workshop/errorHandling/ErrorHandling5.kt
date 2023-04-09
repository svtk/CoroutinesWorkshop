@file:Suppress("DeferredResultUnused")

package com.kotlinconf.workshop.errorHandling

import kotlinx.coroutines.*

// Use supervisor scope to avoid crashing when the child fails
// But the exception is swallowed by SupervisorJob!
suspend fun main() {
    supervisorScope {
        launch { heartbeat() }
        async { doWorkAndThrow() }
    }
}