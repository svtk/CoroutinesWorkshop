package com.kotlinconf.workshop.kettle.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.kotlinconf.workshop.kettle.*
import com.kotlinconf.workshop.kettle.network.KettleService
import com.kotlinconf.workshop.util.log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

// TODO Task
const val ALLOW_UNSTABLE_NETWORK = true

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
        parentScope.coroutineContext + SupervisorJob() + coroutineExceptionHandler
    )

    private val _stableNetwork = mutableStateOf(true)
    val isStableNetwork get() = _stableNetwork
    fun setStableNetwork(stable: Boolean) {
        _stableNetwork.value = stable
        kettleService.changeNetworkStability(stable)
    }

    fun switchOn() {
        scope.launch {
            kettleService.switchOn(100.0.celsius)
        }
    }

    fun switchOff() {
        scope.launch {
            kettleService.switchOff()
        }
    }

    val kettleState: Flow<KettleState> =
        kettleService.observeKettleState()
            .stateIn(scope, SharingStarted.Lazily, KettleState.OFF)

    val celsiusTemperature: Flow<CelsiusTemperature?> =
        kettleService.observeTemperature()
            // initial code: no stateIn
            .stateIn(scope, SharingStarted.Lazily, null)

    val fahrenheitTemperature: Flow<FahrenheitTemperature?> =
        // initial code:
//        flowOf(null)
        celsiusTemperature.map { it?.toFahrenheit() }
}
