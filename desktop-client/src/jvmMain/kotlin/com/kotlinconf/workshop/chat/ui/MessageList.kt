package com.kotlinconf.workshop.chat.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kotlinconf.workshop.ChatMessage

@Composable
internal fun MessageList(
    modifier: Modifier,
    messages: List<ChatMessage>,
    title: String,
    contentAlignment: Alignment,
) {
    Column(
        modifier = modifier.padding(5.dp)
    ) {
        Box(
            contentAlignment = contentAlignment,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(5.dp),
                text = title,
                style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Bold),
            )
        }

        val listState = rememberLazyListState()
        if (messages.isNotEmpty()) {
            LaunchedEffect(messages.last()) {
                listState.animateScrollToItem(messages.lastIndex, scrollOffset = 2)
            }
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            state = listState
        ) {
            messages.forEach { message ->
                item {
                    MessageCard(message, contentAlignment)
                }
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun MessageCard(
    message: ChatMessage,
    contentAlignment: Alignment
) {
    Box(
        contentAlignment = contentAlignment,
        modifier = Modifier.fillMaxWidth()
    ) {
        Card {
            Column {
                Text(
                    text = message.content,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    }
}