package com.kotlinconf.workshop.kettle.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinconf.workshop.kettle.*
import com.kotlinconf.workshop.kettle.network.KettleService
import com.kotlinconf.workshop.kettle.utils.averageOfLast
import com.kotlinconf.workshop.util.log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.time.Duration.Companion.seconds

class KettleViewModel(
    private val kettleService: KettleService,
) : ViewModel() {
    val errorMessage: State<String>
        field = mutableStateOf("")

    private fun showErrorMessage(throwable: Throwable) {
        log("Error occurred: $throwable")
        errorMessage.value = "Can't perform an operation: network error"
        viewModelScope.launch {
            delay(5.seconds)
            errorMessage.value = ""
        }
    }

    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable -> showErrorMessage(throwable) }

    fun switchOn() {
        viewModelScope.launch(coroutineExceptionHandler) {
            kettleService.switchOn()
        }
    }

    fun switchOff() {
        viewModelScope.launch(coroutineExceptionHandler) {
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
//        celsiusTemperature
//            .filterNotNull()
//            .map { it.value }
//            .averageOfLast(5)
//            .map { it.celsius }
}
