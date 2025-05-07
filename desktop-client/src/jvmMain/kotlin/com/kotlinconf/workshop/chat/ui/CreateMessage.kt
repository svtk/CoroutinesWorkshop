package com.kotlinconf.workshop.chat.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import com.kotlinconf.workshop.ChatMessage
import io.chozzle.composemacostheme.MacTheme

@Composable
internal fun CreateMessage(
    onMessageSent: (ChatMessage) -> Unit,
) {
    var message by remember { mutableStateOf("") }
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 10.dp)
            .padding(bottom = 10.dp)
            .onKeyEvent {
                if (it.key != Key.Enter || message.isBlank()) return@onKeyEvent true
                onMessageSent(ChatMessage(message.trim()))
                message = ""
                true
            },
        value = message,
        onValueChange = { message = it },
        label = { Text("Message") },
        trailingIcon = {
            if (message.isNotBlank()) {
                IconButton(
                    onClick = {
                        onMessageSent(ChatMessage(message.trim()))
                        message = ""
                    },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = MacTheme.defaultColors.primary,
                    )
                }
            }
        }
    )
}