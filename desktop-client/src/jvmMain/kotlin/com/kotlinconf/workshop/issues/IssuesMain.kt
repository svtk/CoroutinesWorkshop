package com.kotlinconf.workshop.issues

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.kotlinconf.workshop.issues.network.IssuesService
import com.kotlinconf.workshop.issues.ui.IssuesView
import com.kotlinconf.workshop.issues.ui.IssuesViewModel

@Composable
@Preview
fun App(issuesViewModel: IssuesViewModel) {
    MaterialTheme {
        Box(Modifier.fillMaxSize().padding(10.dp)) {
            IssuesView(issuesViewModel)
        }
    }
}

fun main() = application {
    val issuesViewModel = remember { IssuesViewModel(IssuesService()) }
    Window(
        onCloseRequest = {
            exitApplication()
        },
        title = "Issue Stream Example",
        state = rememberWindowState(width = 500.dp, height = 300.dp),
    ) {
        App(issuesViewModel)
    }
}