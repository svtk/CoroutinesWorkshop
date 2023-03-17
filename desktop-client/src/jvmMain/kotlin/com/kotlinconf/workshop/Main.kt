package com.kotlinconf.workshop

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

@Composable
@Preview
fun App() {

}

fun main() = application {
    Window(
        onCloseRequest = {
            exitApplication()
        },
        title = "Coroutine Workshop",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
    ) {
        App()
    }
}
