package com.kotlinconf.workshop.errorHandling

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyException(message: String) : Exception(message)

fun handleException(throwable: Throwable) {
    println("Handling exception: ${throwable.message}")
}