package com.kotlinconf.workshop.puzzlers

import kotlinx.coroutines.*
import java.io.File
import kotlin.coroutines.CoroutineContext

suspend fun readLargeFilesCancellable() {
    repeat(5) {
        File("$it.bin").readBytes()
        yield()
    }
}


suspend fun readLargeFilesCancellableOK(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
    withContext(dispatcher) {
        repeat(5) {
            File("$it.bin").readBytes()
            ensureActive()
        }
    }
}
