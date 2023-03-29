package com.kotlinconf.workshop.threadsVsCoroutines

import java.lang.Thread.sleep
import kotlin.concurrent.thread

fun main() {
    List(100_000) {
        thread {
            sleep(1000)
            print(".")
        }
    }.forEach { it.join() }
}