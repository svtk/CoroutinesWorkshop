package com.kotlinconf.workshop.util

import com.github.ajalt.mordant.table.horizontalLayout
import com.github.ajalt.mordant.terminal.Terminal

private const val loggerOn = true

fun log(message: Any?) {
    println("${currentTime()} [${Thread.currentThread().name}] $message")
    return
    if (!loggerOn) return
    with(Terminal()) {
        println(
            horizontalLayout {
                cell(theme.info("${currentTime()}"))
                cell(theme.warning("[${Thread.currentThread().name}]"))
                cell(message)
            }
        )
    }
}