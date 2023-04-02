package com.kotlinconf.workshop.articles.ui.views

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kotlinconf.workshop.articles.model.Article
import com.kotlinconf.workshop.articles.ui.ArticlesViewModel
import com.kotlinconf.workshop.blog.User

@Composable
fun ArticlesView(viewModel: ArticlesViewModel) {
    Column {
        Box(Modifier.padding(top = 10.dp).size(32.dp).align(Alignment.CenterHorizontally)) {
            RotatingEmoji()
        }
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
//        ActiveUsersView(
//            activeUsers = viewModel.activeUsers.collectAsState(setOf()).value
//        )
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
        item {
            ThreeColumn("Author", "Title", "Number of Comments", FontWeight.ExtraBold)
        }
        items(articles) { article ->
            Spacer(modifier = Modifier.height(10.dp))
            ArticleView(article)
        }
    }
}

@Composable
fun ThreeColumn(first: String, second: String, third: String, fontWeight: FontWeight = FontWeight.Normal) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0, 0, 0, 20))
            .padding(5.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(0.2f).padding(start = 20.dp),
        ) {
            Text(
                text = first,
                fontWeight = fontWeight
            )
        }
        Box(
            modifier = Modifier.fillMaxWidth(0.8f),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = second,
                fontWeight = fontWeight
            )
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier = Modifier.fillMaxSize(),
                text = third,
                fontWeight = fontWeight
            )
        }
    }
}

@Composable
fun ArticleView(article: Article) {
    ThreeColumn(article.author.name, article.info.title, "${article.comments.size} comments")
}

