package com.kotlinconf.workshop.launcher

import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.kotlinconf.workshop.WorkshopServerConfig.PORT
import kotlinx.coroutines.delay

private const val browserHost = "localhost"
private const val serverRootPath = "/"
private const val serverRootUrl = "http://$browserHost:$PORT$serverRootPath"

@Composable
fun LauncherApp(
    serverStatus: ServerStatus,
    onOpenArticles: () -> Unit,
    onOpenBasicChat: () -> Unit,
    onOpenPriorityChat: () -> Unit,
    onOpenKettle: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    var elapsedSeconds by remember(serverStatus) {
        mutableLongStateOf(
            when (serverStatus) {
                is ServerStatus.Running -> ((System.currentTimeMillis() - serverStatus.startedAtMillis) / 1_000).coerceAtLeast(0)
                else -> 0L
            }
        )
    }

    LaunchedEffect(serverStatus) {
        if (serverStatus is ServerStatus.Running) {
            while (true) {
                elapsedSeconds = ((System.currentTimeMillis() - serverStatus.startedAtMillis) / 1_000).coerceAtLeast(0)
                delay(1_000)
            }
        }
    }

    MaterialTheme {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            when (serverStatus) {
                is ServerStatus.Running -> RunningServerStatus(
                    elapsedSeconds = elapsedSeconds,
                    onOpenApi = { uriHandler.openUri(serverRootUrl) },
                )
                else -> Text(text = serverStatus.message(elapsedSeconds))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            ) {
                LauncherButton(
                    label = "Articles",
                    enabled = serverStatus.canLaunchClients,
                    onClick = onOpenArticles,
                )
                LauncherButton(
                    label = "Basic Chat",
                    enabled = serverStatus.canLaunchClients,
                    onClick = onOpenBasicChat,
                )
                LauncherButton(
                    label = "Chat with Priorities",
                    enabled = serverStatus.canLaunchClients,
                    onClick = onOpenPriorityChat,
                )
                LauncherButton(
                    label = "Kettle",
                    enabled = serverStatus.canLaunchClients,
                    onClick = onOpenKettle,
                )
            }
        }
    }
}

@Composable
private fun LauncherButton(
    label: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Button(onClick = onClick, enabled = enabled) {
        Text(label)
    }
}

@Composable
private fun RunningServerStatus(
    elapsedSeconds: Long,
    onOpenApi: () -> Unit,
) {
    val minutes = elapsedSeconds / 60
    val seconds = elapsedSeconds % 60
    val uptime = "${minutes}m${seconds.toString().padStart(2, '0')}s"
    Row(
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("The server is running on ")
        Text(
            text = "$browserHost:$PORT$serverRootPath",
            color = MaterialTheme.colors.primary,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable(onClick = onOpenApi),
        )
        Text(" for $uptime")
    }
}

sealed interface ServerStatus {
    val canLaunchClients: Boolean

    fun message(elapsedSeconds: Long): String

    data object Starting : ServerStatus {
        override val canLaunchClients = false
        override fun message(elapsedSeconds: Long) = "Starting workshop server..."
    }

    data class Running(val startedAtMillis: Long) : ServerStatus {
        override val canLaunchClients = true

        override fun message(elapsedSeconds: Long): String {
            return "The server is running on $browserHost:$PORT$serverRootPath"
        }
    }

    data class Failed(val error: String) : ServerStatus {
        override val canLaunchClients = false
        override fun message(elapsedSeconds: Long) = "Server failed to start: $error"
    }
}
