package com.kotlinconf.workshop

import com.kotlinconf.workshop.kettle.CelsiusTemperature
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private const val ROOM_TEMPERATURE = 20.0
private const val BOILING_TEMPERATURE = 100.0

class Kettle(
    private val scope: CoroutineScope
) {
    private var switchedOn: Boolean = false
    private var desiredTemperature = BOILING_TEMPERATURE
    private var currentTemperature = ROOM_TEMPERATURE
    private var changingTemperatureJob: Job? = null
    private val mutex = Mutex()
    suspend fun switchOn(desired: CelsiusTemperature = CelsiusTemperature(BOILING_TEMPERATURE)) = mutex.withLock {
        switchedOn = true
        desiredTemperature = desired.value
        changingTemperatureJob?.cancel()
        changingTemperatureJob = scope.launch {
            while (currentTemperature < desiredTemperature) {
                delay(100)
                heatABit()
            }
            delay(1000)
            switchOff()
        }
    }

    suspend fun switchOff() = mutex.withLock {
        switchedOn = false
        changingTemperatureJob?.cancel()
        changingTemperatureJob = scope.launch {
            while (currentTemperature > ROOM_TEMPERATURE) {
                delay(100)
                coolABit()
            }
            if (currentTemperature <= ROOM_TEMPERATURE) {
                cancel()
            }
        }
    }

    private fun heatABit() {
        currentTemperature = (currentTemperature + 0.32).coerceAtMost(desiredTemperature)
    }

    private fun coolABit() {
        currentTemperature = (currentTemperature - 0.02).coerceAtLeast(ROOM_TEMPERATURE)
    }

    fun getTemperature(): CelsiusTemperature {
        return CelsiusTemperature(currentTemperature)
    }
}
