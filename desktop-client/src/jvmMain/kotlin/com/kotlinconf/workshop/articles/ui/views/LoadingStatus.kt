package com.kotlinconf.workshop.articles.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kotlinconf.workshop.articles.ui.ArticlesViewModel
import com.kotlinconf.workshop.articles.ui.ArticlesViewModel.LoadingStatus.*

@Composable
fun LoadingStatus(
    articlesNumber: Int,
    loadingStatus: ArticlesViewModel.LoadingStatus,
    currentLoadingTimeMillis: Long,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.padding(15.dp),
                contentAlignment = Alignment.Center,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        val timeText = currentLoadingTimeMillis.let { time ->
                            if (time == 0L) "" else "${(time / 1000)}.${time % 1000 / 100} sec"
                        }

                        val loadingText = "Loading status: " +
                                when (loadingStatus) {
                                    NOT_STARTED -> "not started"
                                    COMPLETED -> "completed in $timeText"
                                    IN_PROGRESS -> "in progress $timeText"
                                    CANCELED -> "canceled"
                                    FAILED -> "failed"
                                }

                        Text(
                            style = MaterialTheme.typography.subtitle1,
                            text = loadingText
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            style = MaterialTheme.typography.subtitle1,
                            text = "Number of loaded articles: $articlesNumber"
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    when (loadingStatus) {
                        NOT_STARTED, FAILED -> CircularProgressIndicator(0.0f)
                        IN_PROGRESS -> CircularProgressIndicator()
                        COMPLETED -> CircularProgressIndicator(1.0f)
                        CANCELED -> CircularProgressIndicator(0.3f)
                    }
                }
            }
        }
    }
}
