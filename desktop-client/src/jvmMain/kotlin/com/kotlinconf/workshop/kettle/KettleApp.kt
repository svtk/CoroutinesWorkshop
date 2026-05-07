package com.kotlinconf.workshop.kettle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.github.jetbrains.timetracker.androidapp.ui.theme.KettleAppTheme
import com.kotlinconf.workshop.kettle.network.NetworkKettleService
import com.kotlinconf.workshop.kettle.ui.KettleView
import com.kotlinconf.workshop.kettle.ui.KettleViewModel

@Composable
fun KettleApp(kettleViewModel: KettleViewModel) {
    KettleAppTheme {
        KettleView(kettleViewModel)
    }
}

@Composable
fun KettleWindow(
    onCloseRequest: () -> Unit,
    verifyServerOnLaunch: Boolean,
    title: String = "Kettle",
) {
    val kettleService = remember { NetworkKettleService() }
    if (verifyServerOnLaunch) {
        LaunchedEffect(kettleService) {
            kettleService.ensureServerIsRunning()
        }
    }
    Window(
        onCloseRequest = onCloseRequest,
        title = title,
        state = rememberWindowState(width = 200.dp, height = 500.dp),
    ) {
        val kettleViewModel = viewModel { KettleViewModel(kettleService) }
        KettleApp(kettleViewModel)
    }
}

fun main() = application {
    KettleWindow(
        onCloseRequest = ::exitApplication,
        verifyServerOnLaunch = true,
    )
}
