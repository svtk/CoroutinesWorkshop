package com.kotlinconf.workshop.launcher

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.kotlinconf.workshop.articles.ArticlesWindow
import com.kotlinconf.workshop.chat.BasicChatWindow
import com.kotlinconf.workshop.chat.ChatWithPrioritiesWindow
import com.kotlinconf.workshop.kettle.KettleWindow
import kotlinx.coroutines.CancellationException

fun main() = application {
    val launcherState = remember { LauncherState(ServerController()) }

    LaunchedEffect(launcherState) {
        launcherState.startServer()
    }
    DisposableEffect(launcherState) {
        onDispose {
            launcherState.shutdown()
        }
    }

    Window(
        onCloseRequest = {
            launcherState.shutdown()
            exitApplication()
        },
        title = "Coroutines Workshop Launcher",
        state = rememberWindowState(
            position = WindowPosition(Alignment.Center),
            width = 820.dp,
            height = 180.dp,
        ),
    ) {
        LauncherApp(
            serverStatus = launcherState.serverStatus,
            onOpenArticles = launcherState::openArticlesWindow,
            onOpenBasicChat = launcherState::openBasicChatWindow,
            onOpenPriorityChat = launcherState::openPriorityChatWindow,
            onOpenKettle = launcherState::openKettleWindow,
        )
    }

    if (launcherState.isArticlesWindowOpen) {
        ArticlesWindow(
            onCloseRequest = launcherState::closeArticlesWindow,
            verifyServerOnLaunch = false,
        )
    }

    launcherState.basicChatWindowIds.forEach { windowId ->
        key(windowId) {
            BasicChatWindow(
                onCloseRequest = { launcherState.closeBasicChatWindow(windowId) },
                verifyServerOnLaunch = false,
                title = "Basic Chat ${windowId + 1}",
            )
        }
    }

    launcherState.priorityChatWindowIds.forEach { windowId ->
        key(windowId) {
            ChatWithPrioritiesWindow(
                onCloseRequest = { launcherState.closePriorityChatWindow(windowId) },
                verifyServerOnLaunch = false,
                title = "Chat with Priorities ${windowId + 1}",
            )
        }
    }

    launcherState.kettleWindowIds.forEach { windowId ->
        key(windowId) {
            KettleWindow(
                onCloseRequest = { launcherState.closeKettleWindow(windowId) },
                verifyServerOnLaunch = false,
                title = "Kettle ${windowId + 1}",
            )
        }
    }
}

private class LauncherState(
    private val serverController: ServerController,
) {
    var serverStatus by mutableStateOf<ServerStatus>(ServerStatus.Starting)
        private set

    var isArticlesWindowOpen by mutableStateOf(false)
        private set

    val basicChatWindowIds = mutableStateListOf<Int>()
    val priorityChatWindowIds = mutableStateListOf<Int>()
    val kettleWindowIds = mutableStateListOf<Int>()

    private var nextBasicChatWindowId by mutableIntStateOf(0)
    private var nextPriorityChatWindowId by mutableIntStateOf(0)
    private var nextKettleWindowId by mutableIntStateOf(0)

    suspend fun startServer() {
        serverStatus = ServerStatus.Starting
        serverStatus = try {
            serverController.start()
            ServerStatus.Running(startedAtMillis = System.currentTimeMillis())
        } catch (exception: CancellationException) {
            throw exception
        } catch (throwable: Throwable) {
            ServerStatus.Failed(throwable.message ?: throwable::class.simpleName.orEmpty())
        }
    }

    fun shutdown() {
        serverController.stop()
    }

    fun openArticlesWindow() {
        if (serverStatus.canLaunchClients) {
            isArticlesWindowOpen = true
        }
    }

    fun closeArticlesWindow() {
        isArticlesWindowOpen = false
    }

    fun openKettleWindow() {
        if (serverStatus.canLaunchClients) {
            kettleWindowIds += nextKettleWindowId++
        }
    }

    fun closeKettleWindow(windowId: Int) {
        kettleWindowIds -= windowId
    }

    fun openBasicChatWindow() {
        if (serverStatus.canLaunchClients) {
            basicChatWindowIds += nextBasicChatWindowId++
        }
    }

    fun closeBasicChatWindow(windowId: Int) {
        basicChatWindowIds -= windowId
    }

    fun openPriorityChatWindow() {
        if (serverStatus.canLaunchClients) {
            priorityChatWindowIds += nextPriorityChatWindowId++
        }
    }

    fun closePriorityChatWindow(windowId: Int) {
        priorityChatWindowIds -= windowId
    }
}
