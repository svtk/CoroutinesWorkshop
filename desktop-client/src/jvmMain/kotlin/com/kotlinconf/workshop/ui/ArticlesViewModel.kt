package com.kotlinconf.workshop.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kotlinconf.workshop.blog.User
import com.kotlinconf.workshop.model.Article
import com.kotlinconf.workshop.model.findActiveUsers
import com.kotlinconf.workshop.network.BlogService
import com.kotlinconf.workshop.network.BlogServiceBlocking
import com.kotlinconf.workshop.tasks.*
import com.kotlinconf.workshop.ui.ArticlesViewModel.LoadingMode.*
import com.kotlinconf.workshop.ui.ArticlesViewModel.LoadingStatus.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ArticlesViewModel(
    private val blockingService: BlogServiceBlocking,
    private val service: BlogService,
    private val scope: CoroutineScope
) {
    enum class LoadingMode {
        BLOCKING,
        SUSPENDING,
        CONCURRENT,
        NON_CANCELLABLE,
        WITH_ERRORS,
        WITH_PROGRESS,
        WITH_PROGRESS_WITH_ERRORS,
        CONCURRENT_WITH_PROGRESS,
    }

    var loadingMode by mutableStateOf(BLOCKING)
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

    private val _articlesFlow = MutableStateFlow(listOf<Article>())
    val articlesFlow: StateFlow<List<Article>> get() = _articlesFlow

//    private val _activeUsers = MutableStateFlow(setOf<User>())
//    val activeUsers: StateFlow<Set<User>> get() = _activeUsers
    // TODO use as the last task?
    val activeUsers: StateFlow<Set<User>> =
        observeActiveUsers(articlesFlow)
            .stateIn(scope, SharingStarted.Lazily, setOf())


    fun loadComments() {
        clearResults()
        cancellationEnabled = true
        loadingJob = scope.launch {
            val startTime = System.currentTimeMillis()
            when (loadingMode) {
                BLOCKING -> {
                    val articles = blockingService.loadArticles()
                    updateResults(articles, startTime)
                }
                SUSPENDING -> {
                    val articles = service.loadArticles()
                    updateResults(articles, startTime)
                }
                CONCURRENT -> {
                    val articles = service.loadArticlesConcurrently()
                    updateResults(articles, startTime)
                }
                NON_CANCELLABLE -> {
                    val articles = service.loadArticlesNonCancelable()
                    updateResults(articles, startTime)
                }
                WITH_ERRORS -> {
                    val articles = service.loadArticlesUnstable()
                    updateResults(articles, startTime)
                }
                WITH_PROGRESS -> {
                    service.loadArticlesWithProgress()
                        .onEach { updateResults(it, startTime, completed = false) }
                        .onCompletion {
                            markLoadingCompletion(it)
                        }
                        .collect()
                }
                WITH_PROGRESS_WITH_ERRORS -> {
                    service.loadArticlesWithProgressUnstable()
                        .onEach { updateResults(it, startTime, completed = false) }
                        .onCompletion {
                            markLoadingCompletion(it)
                        }
                        .collect()
                }
                CONCURRENT_WITH_PROGRESS -> {
                    service.loadArticlesConcurrentlyWithProgress()
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
        articles: List<Article>,
        startTime: Long,
        completed: Boolean = true,
    ) {
        loadingStatus = if (completed) COMPLETED else IN_PROGRESS
        currentLoadingTimeMillis = System.currentTimeMillis() - startTime
        _articlesFlow.value = articles
//        _activeUsers.value = articles.findActiveUsers()
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
        _articlesFlow.value = listOf()
        loadingStatus = IN_PROGRESS
        newLoadingEnabled = false
        cancellationEnabled = false
        currentLoadingTimeMillis = 0
    }
}