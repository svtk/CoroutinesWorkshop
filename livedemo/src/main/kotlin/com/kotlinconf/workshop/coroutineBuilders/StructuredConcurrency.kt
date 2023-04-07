package com.kotlinconf.workshop.coroutineBuilders.structured

import com.kotlinconf.workshop.util.log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// Option + Shift
// GlobalScope. -- no longer waits
// Dispatcher
// CoroutineName("my")
fun main() {
    runBlocking {
        launch {
            delay(100)
            log("first child completes")
        }
        launch {
            delay(100)
            log("second child completes")
        }
    }
}