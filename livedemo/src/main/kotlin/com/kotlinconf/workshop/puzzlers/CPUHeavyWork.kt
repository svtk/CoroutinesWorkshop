package com.kotlinconf.workshop.puzzlers

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

suspend fun doCpuHeavyWork() {
    Thread.sleep(1000)
}

suspend fun main() {
    coroutineScope {
        launch {
            repeat(10) {
                doCpuHeavyWork()
                yield()
            }
        }
    }
}