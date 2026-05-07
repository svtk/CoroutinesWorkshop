package com.kotlinconf.workshop.articles

import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kotlinconf.workshop.articles.network.BlogServiceBlocking
import com.kotlinconf.workshop.articles.network.createBlogService
import com.kotlinconf.workshop.articles.ui.ArticlesViewModel
import com.kotlinconf.workshop.articles.ui.views.ArticlesView
import com.kotlinconf.workshop.network.WorkshopKtorService

@Composable
fun ArticlesApp(viewModel: ArticlesViewModel) {
    MaterialTheme {
        ProvideTextStyle(LocalTextStyle.current.copy(letterSpacing = 0.sp)) {
            ArticlesView(viewModel)
        }
    }

}

@Composable
fun ArticlesWindow(
    onCloseRequest: () -> Unit,
    verifyServerOnLaunch: Boolean,
) {
    val blogService = remember { createBlogService() }
    if (verifyServerOnLaunch) {
        LaunchedEffect(blogService) {
            (blogService as WorkshopKtorService).ensureServerIsRunning()
        }
    }
    Window(
        onCloseRequest = onCloseRequest,
        title = "Articles",
        state = rememberWindowState(width = 760.dp, height = 760.dp),
    ) {
        val viewModel = viewModel {
            ArticlesViewModel(
                blockingService = BlogServiceBlocking(),
                service = blogService,
            )
        }
        ArticlesApp(viewModel)
    }
}

fun main() = application {
    ArticlesWindow(
        onCloseRequest = ::exitApplication,
        verifyServerOnLaunch = true,
    )
}
