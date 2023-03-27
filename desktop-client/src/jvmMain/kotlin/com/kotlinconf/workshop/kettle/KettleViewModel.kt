package com.kotlinconf.workshop.kettle

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.kotlinconf.workshop.network.KettleService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
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

    val temperature: StateFlow<Temperature> = kettleService
        .observeTemperature()
        .stateIn(scope, SharingStarted.Lazily, 20.0.celsius)
}
