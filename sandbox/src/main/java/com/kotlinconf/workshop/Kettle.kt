package com.kotlinconf.workshop

import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.milliseconds

private val ROOM_TEMPERATURE = 20.0
private val BOILING_TEMPERATURE = 100.0

class Kettle(
    private val scope: CoroutineScope
) {
    private var switchedOn: Boolean = false
    private var desiredTemperature = BOILING_TEMPERATURE
    private var currentTemperature = ROOM_TEMPERATURE
    private var heatingJob: Job? = null
    private var coolingJob: Job? = null
    fun switchOn(desiredTemperature: Double = BOILING_TEMPERATURE) {
        switchedOn = true
        this.desiredTemperature = desiredTemperature
        coolingJob?.cancel()
        heatingJob = scope.launch {
            while (currentTemperature < desiredTemperature) {
                delay(100)
                heatABit()
            }
            delay(200)
            switchOff()
        }
    }

    fun switchOff() {
        switchedOn = false
        heatingJob?.cancel()
        coolingJob = scope.launch {
            while (currentTemperature > ROOM_TEMPERATURE) {
                delay(100)
                coolABit()
            }
            if (currentTemperature <= ROOM_TEMPERATURE) {
                delay(500)
                cancel()
            }
        }
    }

    private fun heatABit() {
        currentTemperature = (currentTemperature + 0.64).coerceAtMost(desiredTemperature)
    }

    private fun coolABit() {
        currentTemperature = (currentTemperature - 0.02).coerceAtLeast(ROOM_TEMPERATURE)
    }

    fun getTemperature(): Double {
        return currentTemperature
    }
}

suspend fun main() = coroutineScope {
        val kettle = Kettle(this)
        kettle.switchOn(desiredTemperature = 80.0)
        var temperature: Double
        val startTime = System.currentTimeMillis()
        while(true) {
            temperature = kettle.getTemperature()

            val time = (System.currentTimeMillis() - startTime).milliseconds
            print("\rTemperature: $temperature. Time: ${time.inWholeSeconds} seconds")
            delay(200)
        }
}
