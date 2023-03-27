package com.kotlinconf.workshop.kettle

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.kotlinconf.workshop.network.KettleService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class KettleViewModel(
    private val kettleService: KettleService,
    private val scope: CoroutineScope,
) {
    private val _isWorking = mutableStateOf(false)
    val isWorking: State<Boolean> get() = _isWorking

    fun switchOn() {
        _isWorking.value = true
        scope.launch {
            kettleService.switchOn(100.0.celsius)
        }
    }

    fun switchOff() {
        _isWorking.value = false
        scope.launch {
            kettleService.switchOff()
        }
    }

    val celsiusTemperature: Flow<CelsiusTemperature?> =
        kettleService.observeTemperatureViaWebsockets()
        // initial code (no stateIn):
        //.stateIn(scope, SharingStarted.Lazily, null)

    val fahrenheitTemperature: Flow<FahrenheitTemperature?> =
        // initial code:
        flowOf(null)
//        celsiusTemperature.map { it?.toFahrenheit() }
}
