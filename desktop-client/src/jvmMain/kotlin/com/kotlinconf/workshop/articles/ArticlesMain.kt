package com.kotlinconf.workshop.articles

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.kotlinconf.workshop.articles.network.BlogService
import com.kotlinconf.workshop.articles.network.createBlogServiceBlocking
import com.kotlinconf.workshop.articles.ui.views.ArticlesView
import com.kotlinconf.workshop.articles.ui.ArticlesViewModel

@Composable
@Preview
fun App(viewModel: ArticlesViewModel) {
    ArticlesView(viewModel)
}

fun main() = application {
    val scope = rememberCoroutineScope()
    val viewModel = ArticlesViewModel(
        blockingService = createBlogServiceBlocking(),
        service = BlogService(),
        scope = scope
    )
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
