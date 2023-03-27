package com.kotlinconf.workshop.util

import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

//val log: Logger = LoggerFactory.getLogger("IssueTracker")
private const val loggerOn = true

fun log(message: String?) {
//    log.info(message)
    if (!loggerOn) return
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val time = LocalTime(
        hour = now.hour,
        minute = now.minute,
        second = now.second,
        nanosecond = (now.nanosecond / 1000000) * 1000000
    )
    println("$time [${Thread.currentThread().name}] $message")
}