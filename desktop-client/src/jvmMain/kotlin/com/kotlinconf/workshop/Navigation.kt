package com.kotlinconf.workshop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.Icon
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.kotlinconf.workshop.ui.ArticlesView
import com.kotlinconf.workshop.articles.ArticlesViewModel

enum class TaskScreen(
    val label: String,
    val icon: ImageVector = Icons.Filled.Task
) {
    Task1(label = "Task 1"),
    Task2(label = "Task 2"),
    Task3(label = "Task 3"),
    Task4(label = "Task 4"),
    Task5(label = "Task 5"),
    Task6(label = "Task 6"),
}

@Composable
fun MainTasksView(viewModel: ArticlesViewModel) {
    val screens = TaskScreen.values()
    var currentScreen by remember { mutableStateOf(TaskScreen.Task1) }
    Row {
        NavigationRail {
            screens.forEach {
                NavigationRailItem(
                    selected = currentScreen == it,
                    icon = {
                        Icon(
                            imageVector = it.icon,
                            contentDescription = it.label
                        )
                    },
                    label = {
                        Text(it.label)
                    },
                    onClick = {
                        currentScreen = it
                    }
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxHeight()
        ) {
            ArticlesView(viewModel)
        }
    }
}