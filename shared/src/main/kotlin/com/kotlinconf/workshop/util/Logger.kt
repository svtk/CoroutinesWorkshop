package com.kotlinconf.workshop.util

fun log(message: Any?) {
    println("[CLIENT] ${currentTime()} [${Thread.currentThread().name}] $message")
}