package com.kotlinconf.workshop.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

val log: Logger = LoggerFactory.getLogger("IssueTracker")

fun log(message: String?) {
    log.info(message)
//    println("[${Thread.currentThread().name}] $message")
}