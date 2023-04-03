package com.kotlinconf.workshop.kettle.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kotlinconf.workshop.kettle.CelsiusTemperature
import com.kotlinconf.workshop.kettle.FahrenheitTemperature
import com.kotlinconf.workshop.kettle.KettlePowerState
import com.kotlinconf.workshop.kettle.celsius

@Composable
fun KettleView(kettleViewModel: KettleViewModel) {
    KettleView(
        kettleViewModel.kettlePowerState.collectAsState(KettlePowerState.OFF).value,
        kettleViewModel.isStableNetwork.value,
        kettleViewModel::setStableNetwork,
        kettleViewModel.errorMessage.value,
        kettleViewModel.celsiusTemperature.collectAsState(null).value,
        kettleViewModel.fahrenheitTemperature.collectAsState(null).value,
        kettleViewModel.smoothCelsiusTemperature.collectAsState(0.0.celsius).value,
        kettleViewModel::switchOn,
        kettleViewModel::switchOff,
    )
}

@Composable
fun KettleView(
    kettlePowerState: KettlePowerState,
    isStable: Boolean,
    onStableChange: (Boolean) -> Unit,
    errorMessage: String,
    celsiusTemperature: CelsiusTemperature?,
    fahrenheitTemperature: FahrenheitTemperature?,
    smoothedCelsiusTemperature: CelsiusTemperature,
    switchOn: () -> Unit,
    switchOff: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Row {
            ActionButton(
                modifier = Modifier.fillMaxWidth(0.5f),
                contentDescription = "Start",
                imageVector = Icons.Filled.PlayCircle,
                color = MaterialTheme.colors.secondary,
                onClick = switchOn,
                enabled = kettlePowerState == KettlePowerState.OFF,
            )
            ActionButton(
                modifier = Modifier.fillMaxWidth(),
                contentDescription = "Finish",
                imageVector = Icons.Filled.StopCircle,
                color = MaterialTheme.colors.primary,
                onClick = switchOff,
                enabled = kettlePowerState == KettlePowerState.ON,
            )

            Button(
                onClick = switchOn,
                enabled = kettlePowerState == KettlePowerState.OFF,
            ) {
                Text("On")
            }
            Button(
                onClick = switchOff,
                enabled = kettlePowerState == KettlePowerState.ON,
            ) {
                Text("Off")
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isStable,
                onCheckedChange = onStableChange,
                enabled = ALLOW_UNSTABLE_NETWORK
            )
            Text("Stable network")
        }
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colors.error, style = MaterialTheme.typography.caption)
        }
        Text(
            modifier = Modifier.padding(20.dp),
            text = "Temperature:",
            style = MaterialTheme.typography.h6
        )
        Text("${celsiusTemperature ?: '?'} C", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(10.dp))
        Text("${fahrenheitTemperature ?: '?'} F", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier.padding(20.dp),
            text = "Smoothed temperature:",
            style = MaterialTheme.typography.h6
        )
        Text("$smoothedCelsiusTemperature C", style = MaterialTheme.typography.h4)
    }
}

@Composable
fun ActionButton(
    modifier: Modifier,
    contentDescription: String,
    imageVector: ImageVector,
    color: Color,
    onClick: () -> Unit,
    enabled: Boolean,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier.size(50.dp),
            tint = if (enabled) color else Color.Gray,
        )
    }
}
