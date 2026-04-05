@file:OptIn(ExperimentalAtomicApi::class)

package com.kotlinconf.workshop.coroutineBuilders

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.incrementAndFetch

suspend fun main() {
    val x = AtomicInt(0)
    coroutineScope {
        repeat(10_000) {
            launch {
                x.incrementAndFetch()
            }
        }
    }
    println(x)
}