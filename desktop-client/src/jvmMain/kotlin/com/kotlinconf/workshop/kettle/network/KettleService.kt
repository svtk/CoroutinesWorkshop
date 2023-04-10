package com.kotlinconf.workshop.kettle.network

import com.kotlinconf.workshop.kettle.CelsiusTemperature
import com.kotlinconf.workshop.kettle.KettlePowerState
import kotlinx.coroutines.flow.Flow

interface KettleService {
    suspend fun switchOn()
    suspend fun switchOff()
    fun observeTemperature(): Flow<CelsiusTemperature?>
    fun observeKettlePowerState(): Flow<KettlePowerState>
}