package com.kotlinconf.workshop.util

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal fun currentTime(): LocalTime {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val time = LocalTime(
        hour = now.hour,
        minute = now.minute,
        second = now.second,
        nanosecond = (now.nanosecond / 1000000) * 1000000
    )
    return time
}