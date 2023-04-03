package com.kotlinconf.workshop.articles

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.kotlinconf.workshop.articles.network.BlogServiceBlocking
import com.kotlinconf.workshop.articles.network.createBlogService
import com.kotlinconf.workshop.articles.ui.ArticlesViewModel
import com.kotlinconf.workshop.articles.ui.views.ArticlesView

@Composable
@Preview
fun App(viewModel: ArticlesViewModel) {
    ArticlesView(viewModel)
}

fun main() = application {
    val coroutineScope = rememberCoroutineScope()
    val viewModel = remember {
        ArticlesViewModel(
            blockingService = BlogServiceBlocking(),
            service = createBlogService(),
            parentScope = coroutineScope
        )
    }
    Window(
        onCloseRequest = {
            exitApplication()
        },
        title = "Coroutine Workshop",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
    ) {
        MaterialTheme() {
            ProvideTextStyle(LocalTextStyle.current.copy(letterSpacing = 0.sp)) {
                App(viewModel)
            }
        }
    }
}
