package com.kotlinconf.workshop.issues.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun IssuesView(viewModel: IssuesViewModel) {
    val issues = viewModel.issueFlow.collectAsState()
    val comments = viewModel.commentFlow.collectAsState()
    Column(Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "New Issues / New Comments",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Row {
            LazyColumn(Modifier.fillMaxWidth(0.5f).fillMaxHeight()) {
                items(issues.value.asReversed()) {
                    Text(
                        modifier = Modifier.background(Color.White).border(1.dp, Color.Black).padding(8.dp),
                        text = it.title
                    )
                }
            }
            LazyColumn(Modifier.fillMaxWidth().fillMaxHeight()) {
                items(comments.value.asReversed()) {
                    Text(
                        modifier = Modifier.background(Color.White).border(1.dp, Color.Black).padding(8.dp),
                        text = it.content
                    )
                }
            }
        }
    }
}