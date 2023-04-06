package com.kotlinconf.workshop.util

private const val loggerOn = true

fun log(message: Any?) {
    if (!loggerOn) return
    println("${currentTime()} [${Thread.currentThread().name}] $message")
}