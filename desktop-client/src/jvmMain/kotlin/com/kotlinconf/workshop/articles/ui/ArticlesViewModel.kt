package com.kotlinconf.workshop.articles.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kotlinconf.workshop.articles.model.Article
import com.kotlinconf.workshop.articles.network.BlogService
import com.kotlinconf.workshop.articles.network.BlogServiceBlocking
import com.kotlinconf.workshop.articles.tasks.*
import com.kotlinconf.workshop.articles.ui.ArticlesViewModel.LoadingStatus.*
import com.kotlinconf.workshop.articles.ui.LoadingMode.*
import com.kotlinconf.workshop.articles.util.loadStoredMode
import com.kotlinconf.workshop.articles.util.removeStoredMode
import com.kotlinconf.workshop.articles.util.saveParams
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class ArticlesViewModel(
    private val blockingService: BlogServiceBlocking,
    private val service: BlogService,
    private val scope: CoroutineScope
) {
    var loadingMode by mutableStateOf(BLOCKING)
        private set

    fun changeLoadingMode(loadingMode: LoadingMode) {
        this.loadingMode = loadingMode
    }

    enum class LoadingStatus { NOT_STARTED, IN_PROGRESS, COMPLETED, CANCELED, FAILED }

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

    init {
        try {
            changeLoadingMode(loadStoredMode())
        } catch (e: Exception) {
            e.printStackTrace()
            removeStoredMode()
        }
    }


    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        markLoadingCompletion(exception)
    }

    fun loadComments() {
        saveParams(loadingMode)
        clearResults()
        cancellationEnabled = true
        loadingJob = scope.launch(SupervisorJob() + coroutineExceptionHandler) {
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
                UNSTABLE -> {
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
                CONCURRENT_WITH_PROGRESS -> {
                    service.loadArticlesConcurrentlyWithProgressFlow()
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
        if (completed) {
            markLoadingCompletion()
        }
    }

    private fun markLoadingCompletion(throwable: Throwable? = null) {
        loadingStatus = when {
            throwable is CancellationException -> CANCELED
            throwable != null -> FAILED
            else -> COMPLETED
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