@file:Suppress("DeferredResultUnused")

package com.kotlinconf.workshop.errorHandling

import kotlinx.coroutines.*

// Because it's coroutineScope, failed coroutine crashes its parent
// Adding throw-catch won't help!
suspend fun main() {
    coroutineScope {
        launch { heartbeat() }
        val deferred = async { doWorkAndThrow() }
        launch {
            try {
                deferred.await()
            } catch (e: MyException) {
                handleException(e)
            }
        }
    }
}
