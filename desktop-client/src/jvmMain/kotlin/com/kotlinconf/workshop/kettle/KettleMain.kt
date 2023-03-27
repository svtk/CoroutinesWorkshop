package com.kotlinconf.workshop.kettle

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.kotlinconf.workshop.network.KettleService

@Composable
@Preview
fun App(kettleViewModel: KettleViewModel) {
    KettleView(kettleViewModel)
}

fun main() = application {
    val scope = rememberCoroutineScope()
    val kettleViewModel = KettleViewModel(KettleService(), scope)
    Window(
        onCloseRequest = {
            exitApplication()
        },
        title = "Kettle Flow Example",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
    ) {
        App(kettleViewModel)
    }
}
