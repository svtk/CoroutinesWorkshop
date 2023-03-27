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
import com.kotlinconf.workshop.blog.User
import com.kotlinconf.workshop.model.Article
import ui.LoadingControls

@Composable
fun ArticlesView(viewModel: ArticlesViewModel) {
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
        val articles = viewModel.articlesFlow.collectAsState(listOf()).value
        LoadingStatus(
            articlesNumber = articles.size,
            loadingStatus = viewModel.loadingStatus,
            currentLoadingTimeMillis = viewModel.currentLoadingTimeMillis,
        )
        ActiveUsersView(
            activeUsers = viewModel.activeUsers.collectAsState(setOf()).value
        )
        ArticlesView(
            articles = articles,
        )
    }
}

@Composable
fun ActiveUsersView(activeUsers: Set<User>) {
    val text = if (activeUsers.isNotEmpty()) {
        "Active users: " + activeUsers.joinToString { it.name }
    }
    else ""
    Text(text)
}

@Composable
fun ArticlesView(
    articles: List<Article>
) {
    LazyColumn {
        items(articles) { article ->
            Spacer(modifier = Modifier.height(10.dp))
            ArticleView(article)
        }
    }
}

@Composable
fun ArticleView(article: Article) {
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
                text = article.author.name,
            )
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = article.info.title,
            )
        }
    }
}

