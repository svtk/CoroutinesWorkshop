package com.kotlinconf.workshop

import com.kotlinconf.workshop.kettle.Temperature
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

suspend fun main() {
    val kettleService = KettleService()
    kettleService.switchOn(Temperature(90.0))
    var temperature: Temperature
    val startTime = System.currentTimeMillis()
    while(true) {
        temperature = kettleService.getTemperature()

        val time = (System.currentTimeMillis() - startTime).milliseconds
        print("\rTemperature: ${temperature.value.toInt()}. Time: ${time.inWholeSeconds} seconds")
        delay(200)
    }
}