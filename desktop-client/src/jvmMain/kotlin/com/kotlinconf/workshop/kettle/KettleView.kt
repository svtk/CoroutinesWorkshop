package com.kotlinconf.workshop.kettle

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun KettleView(kettleViewModel: KettleViewModel) {
    KettleView(
        kettleViewModel.isWorking.value,
        kettleViewModel.celsiusTemperature.collectAsState(null).value,
        kettleViewModel.fahrenheitTemperature.collectAsState(null).value,
        kettleViewModel::switchOn,
        kettleViewModel::switchOff,
    )
}

@Composable
fun KettleView(
    isWorking: Boolean,
    celsiusTemperature: CelsiusTemperature?,
    fahrenheitTemperature: FahrenheitTemperature?,
    switchOn: () -> Unit,
    switchOff: () -> Unit,
) {
    Column {
        Button(
            onClick = switchOn,
            enabled = !isWorking,
        ) {
            Text("On")
        }
        Button(
            onClick = switchOff,
            enabled = isWorking,
        ) {
            Text("Off")
        }
        Text("Temperature")
        if (celsiusTemperature != null) {
            Text("$celsiusTemperature Celsius")
        }
        if (fahrenheitTemperature != null) {
            Text("$fahrenheitTemperature Fahrenheit")
        }
    }
}