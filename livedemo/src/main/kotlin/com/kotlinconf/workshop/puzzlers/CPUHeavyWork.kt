package com.kotlinconf.workshop.puzzlers

import kotlinx.coroutines.yield

fun doCpuHeavyWork() {
    Thread.sleep(1000)
}


suspend fun runHeavyTasks() {
    repeat(10) {
        doCpuHeavyWork()
        yield() // let other coroutines run
    }
}

suspend fun main() {
    runHeavyTasks()
}