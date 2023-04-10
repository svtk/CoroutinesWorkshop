package com.kotlinconf.workshop.kettle.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.kotlinconf.workshop.kettle.*
import com.kotlinconf.workshop.kettle.network.KettleService
import com.kotlinconf.workshop.kettle.utils.averageOfLast
import com.kotlinconf.workshop.util.log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class KettleViewModel(
    private val kettleService: KettleService,
    parentScope: CoroutineScope,
) {
    private val _errorMessage = mutableStateOf("")
    val errorMessage: State<String> get() = _errorMessage
    private fun showErrorMessage(throwable: Throwable) {
        log("Error occurred: $throwable")
        _errorMessage.value = "Can't perform an operation: network error"
        scope.launch {
            delay(5000)
            _errorMessage.value = ""
        }
    }

    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable -> showErrorMessage(throwable) }

    private val scope = CoroutineScope(
        // initial code:
//        parentScope.coroutineContext
        parentScope.coroutineContext + SupervisorJob() + coroutineExceptionHandler
    )

    fun switchOn() {
        scope.launch {
            kettleService.switchOn()
        }
    }

    fun switchOff() {
        scope.launch {
            kettleService.switchOff()
        }
    }

    val kettlePowerState: Flow<KettlePowerState> =
        kettleService.observeKettlePowerState()
//            .stateIn(scope, SharingStarted.Lazily, KettleState.OFF)
            .shareIn(scope, SharingStarted.Lazily)

    val celsiusTemperature: Flow<CelsiusTemperature?> =
        kettleService.observeTemperature()
            // initial code: no stateIn
            .shareIn(scope, SharingStarted.Lazily)
//            .stateIn(scope, SharingStarted.Lazily, null)

    val fahrenheitTemperature: Flow<FahrenheitTemperature?> =
    // initial code:
//        flowOf(null)
        celsiusTemperature.map { it?.toFahrenheit() }

    val smoothCelsiusTemperature: Flow<CelsiusTemperature> =
        celsiusTemperature
            .filterNotNull()
            .map { it.value }
            .averageOfLast(5)
            .map { it.celsius }
}
