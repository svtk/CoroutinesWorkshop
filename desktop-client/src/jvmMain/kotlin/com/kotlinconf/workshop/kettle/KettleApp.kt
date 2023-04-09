package com.kotlinconf.workshop.kettle

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.github.jetbrains.timetracker.androidapp.ui.theme.KettleAppTheme
import com.kotlinconf.workshop.kettle.network.NetworkKettleService
import com.kotlinconf.workshop.kettle.ui.KettleView
import com.kotlinconf.workshop.kettle.ui.KettleViewModel

@Composable
@Preview
fun KettleApp(kettleViewModel: KettleViewModel) {
    KettleAppTheme {
        KettleView(kettleViewModel)
    }
}

fun main() = application {
    val scope = rememberCoroutineScope()
    val kettleService = remember { NetworkKettleService() }
    val kettleViewModel = remember { KettleViewModel(kettleService, scope) }
    LaunchedEffect(true) {
        kettleService.ensureServerIsRunning()
    }
    Window(
        onCloseRequest = {
            exitApplication()
        },
        title = "Kettle Flow Example",
        state = rememberWindowState(width = 200.dp, height = 500.dp),
    ) {
        KettleApp(kettleViewModel)
    }
}
