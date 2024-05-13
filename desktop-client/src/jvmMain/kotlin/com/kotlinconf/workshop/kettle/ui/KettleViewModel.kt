package com.kotlinconf.workshop.kettle.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.kotlinconf.workshop.kettle.*
import com.kotlinconf.workshop.kettle.network.KettleService
import com.kotlinconf.workshop.kettle.utils.averageOfLast
import com.kotlinconf.workshop.util.log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.time.Duration.Companion.seconds

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
            delay(5.seconds)
            _errorMessage.value = ""
        }
    }

    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable -> showErrorMessage(throwable) }

    private val scope = CoroutineScope(
        parentScope.coroutineContext
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

    val celsiusTemperature: Flow<CelsiusTemperature?> =
        kettleService.observeTemperature()

    val fahrenheitTemperature: Flow<FahrenheitTemperature?> =
        flowOf(null)

    val smoothCelsiusTemperature: Flow<CelsiusTemperature?> =
        flowOf(null)
//        celsiusTemperature.map {
//            it.value
//        }.averageOfLast(5).map {
//            CelsiusTemperature(it)
//        }
}
