package com.kotlinconf.workshop.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kotlinconf.workshop.Comment
import ui.LoadingControls

@Composable
fun CommentsView(viewModel: ViewModel) {
    Column {
        LoadingControls(
            modifier = Modifier,
            loadingMode = viewModel.loadingMode,
            updateLoadingMode = viewModel::changeLoadingMode,
            startLoading = viewModel::loadComments,
            newLoadingEnabled = viewModel.newLoadingEnabled,
            cancelLoading = viewModel::cancelLoading,
            cancellationEnabled = viewModel.cancellationEnabled
        )
        val comments = viewModel.comments.collectAsState(listOf()).value
        LoadingStatus(
            commentsNumber = comments.size,
            loadingStatus = viewModel.loadingStatus,
            currentLoadingTimeMillis = viewModel.currentLoadingTimeMillis,
        )
        CommentsView(
            comments = comments,
        )
    }
}
@Composable
fun CommentsView(
    comments: List<Comment>
) {
    LazyColumn {
        items(comments) { comment ->
            Spacer(modifier = Modifier.height(10.dp))
            CommentView(comment)
        }
    }
}

@Composable
fun CommentView(comment: Comment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0, 0, 0, 20))
            .padding(5.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(0.5f).padding(start = 20.dp),
        ) {
            Text(
                text = comment.author.name,
            )
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = comment.text,
            )
        }
    }
}

