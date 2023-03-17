package com.kotlinconf.workshop.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kotlinconf.workshop.Comment
import com.kotlinconf.workshop.User
import com.kotlinconf.workshop.data.IssueTrackerService
import com.kotlinconf.workshop.tasks.getCommentsByUser
import com.kotlinconf.workshop.tasks.getCommentsByUserAsync
import com.kotlinconf.workshop.tasks.getCommentsByUserAsyncWithProgress
import com.kotlinconf.workshop.tasks.getCommentsByUserWithProgress
import com.kotlinconf.workshop.ui.ViewModel.LoadingMode.*
import com.kotlinconf.workshop.ui.ViewModel.LoadingStatus.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ViewModel(
    private val service: IssueTrackerService,
    private val scope: CoroutineScope
) {
    enum class LoadingMode { SUSPEND, CONCURRENT, PROGRESS, PROGRESS_CONCURRENT }

    var loadingMode by mutableStateOf(SUSPEND)
        private set

    fun changeLoadingMode(loadingMode: LoadingMode) {
        this.loadingMode = loadingMode
    }

    enum class LoadingStatus { NOT_STARTED, IN_PROGRESS, COMPLETED, CANCELED }

    var loadingStatus by mutableStateOf(NOT_STARTED)
        private set

    var currentLoadingTimeMillis by mutableStateOf(0L)
        private set

    var newLoadingEnabled by mutableStateOf(true)
        private set

    var cancellationEnabled by mutableStateOf(false)
        private set

    private var loadingJob by mutableStateOf<Job?>(null)

    private val _comments = MutableStateFlow(listOf<Comment>())
    val comments: StateFlow<List<Comment>> get() = _comments

    fun loadComments() {
        clearResults()
        cancellationEnabled = true
        loadingJob = scope.launch {
            val user = User("User 1") // TODO
            val startTime = System.currentTimeMillis()
            when (loadingMode) {
                SUSPEND -> {
                    val comments = service.getCommentsByUser(user)
                    updateResults(comments, startTime, completed = true)
                }
                CONCURRENT -> {
                    val comments = service.getCommentsByUserAsync(user)
                    updateResults(comments, startTime, completed = true)
                }
                PROGRESS -> {
                    service.getCommentsByUserWithProgress(user)
                        .onEach { updateResults(it, startTime, completed = false) }
                        .onCompletion {
                            markLoadingCompletion(it)
                        }
                        .collect()
                }
                PROGRESS_CONCURRENT -> {
                    service.getCommentsByUserAsyncWithProgress(user)
                        .onEach { updateResults(it, startTime, completed = false) }
                        .onCompletion {
                            markLoadingCompletion(it)
                        }
                        .collect()
                }
            }
        }
    }

    private fun updateResults(
        comments: List<Comment>,
        startTime: Long,
        completed: Boolean = true,
    ) {
        loadingStatus = if (completed) COMPLETED else IN_PROGRESS
        currentLoadingTimeMillis = System.currentTimeMillis() - startTime
        _comments.value = comments
        if (completed) {
            markLoadingCompletion()
        }
    }

    private fun markLoadingCompletion(throwable: Throwable? = null) {
        if (throwable != null) {
            loadingStatus = CANCELED
        }
        else {
            loadingStatus = COMPLETED
        }
        newLoadingEnabled = true
        cancellationEnabled = false
    }

    fun cancelLoading() {
        loadingJob?.cancel()
        loadingStatus = CANCELED
        newLoadingEnabled = true
        cancellationEnabled = false
        currentLoadingTimeMillis = 0
    }

    private fun clearResults() {
        _comments.value = listOf()
        loadingStatus = IN_PROGRESS
        newLoadingEnabled = false
        cancellationEnabled = false
        currentLoadingTimeMillis = 0
    }
}