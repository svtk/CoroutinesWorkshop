package com.kotlinconf.workshop.data

import com.kotlinconf.workshop.kettle.CelsiusTemperature
import com.kotlinconf.workshop.kettle.KettlePowerState
import com.kotlinconf.workshop.kettle.celsius
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private val ROOM_TEMPERATURE = 20.0.celsius
private val BOILING_TEMPERATURE = 100.0.celsius

class Kettle(
    private val scope: CoroutineScope
) {
    private var kettlePowerState = MutableStateFlow(KettlePowerState.OFF)
    private var desiredTemperature = BOILING_TEMPERATURE
    private var currentTemperature = ROOM_TEMPERATURE
    private var changingTemperatureJob: Job? = null
    private val mutex = Mutex()
    suspend fun switchOn(desired: CelsiusTemperature = BOILING_TEMPERATURE) = mutex.withLock {
        kettlePowerState.value = KettlePowerState.ON
        desiredTemperature = desired
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
        kettlePowerState.value = KettlePowerState.OFF
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
        return currentTemperature
    }

    fun observeKettleState(): Flow<KettlePowerState> {
        return kettlePowerState
    }
}