package com.kotlinconf.workshop

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.kotlinconf.workshop.data.IssueTrackerService
import com.kotlinconf.workshop.ui.CommentsView
import com.kotlinconf.workshop.ui.ViewModel

@Composable
@Preview
fun App(viewModel: ViewModel) {
    CommentsView(viewModel)
}

fun main() = application {
    val scope = rememberCoroutineScope()
    val viewModel = ViewModel(IssueTrackerService(), scope)
    Window(
        onCloseRequest = {
            exitApplication()
        },
        title = "Coroutine Workshop",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
    ) {
        App(viewModel)
    }
}
