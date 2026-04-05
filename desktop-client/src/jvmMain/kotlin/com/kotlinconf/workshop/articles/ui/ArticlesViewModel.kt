package com.kotlinconf.workshop.articles.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinconf.workshop.articles.model.Article
import com.kotlinconf.workshop.articles.network.BlogService
import com.kotlinconf.workshop.articles.network.BlogServiceBlocking
import com.kotlinconf.workshop.articles.tasks.*
import com.kotlinconf.workshop.articles.ui.ArticlesViewModel.LoadingStatus.*
import com.kotlinconf.workshop.articles.ui.LoadingMode.*
import com.kotlinconf.workshop.articles.util.loadStoredMode
import com.kotlinconf.workshop.articles.util.removeStoredMode
import com.kotlinconf.workshop.articles.util.saveParams
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class ArticlesViewModel(
    private val blockingService: BlogServiceBlocking,
    private val service: BlogService,
) : ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        markLoadingCompletion(exception)
    }

    // Task: Use SupervisorJob for the dedicated loadingScope.
    // Replace Job(...) with SupervisorJob(...) so a failed loading attempt in LoadingMode.UNSTABLE_NETWORK
    // doesn't permanently cancel the loadingScope and break later loading attempts.
    // initial code: Job(viewModelScope.coroutineContext.job)
    private val loadingScope =
        CoroutineScope(SupervisorJob(viewModelScope.coroutineContext.job))

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

    val articlesFlow: StateFlow<List<Article>>
        field = MutableStateFlow(listOf())

    init {
        try {
            changeLoadingMode(loadStoredMode())
        } catch (e: Exception) {
            e.printStackTrace()
            removeStoredMode()
        }
    }

    fun loadComments() {
        saveParams(loadingMode)
        clearResults()
        cancellationEnabled = true
        loadingJob = loadingScope.launch(coroutineExceptionHandler) {
            val startTime = System.currentTimeMillis()
            when (loadingMode) {
                BLOCKING -> {
                    val articleList = loadArticles(blockingService)
                    updateResults(articleList, startTime)
                }

                SUSPENDING -> {
                    val articleList = loadArticles(service)
                    updateResults(articleList, startTime)
                }

                CONCURRENT -> {
                    val articleList = loadArticlesConcurrently(service)
                    updateResults(articleList, startTime)
                }

                NON_CANCELABLE -> {
                    val articleList = loadArticlesNonCancelable(service)
                    updateResults(articleList, startTime)
                }

                WITH_PROGRESS -> {
                    val articleFlow = observeArticlesLoading(service)
                    updateResultsWithProgress(articleFlow, startTime)
                }

                CONCURRENT_WITH_PROGRESS -> {
                    val articleFlow = observeArticlesConcurrently(service)
                    updateResultsWithProgress(articleFlow, startTime)
                }

                UNSTABLE_NETWORK -> {
                    val articles = observeArticlesUnstable(service)
                    updateResultsWithProgress(articles, startTime)
                }

                UNSTABLE_WITH_RETRY -> {
                    val articles = observeArticlesUnstableWithRetry(service)
                    updateResultsWithProgress(articles, startTime)
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
        articlesFlow.value = articles
        if (completed) {
            markLoadingCompletion()
        }
    }

    private suspend fun updateResultsWithProgress(
        articleFlow: Flow<Article>,
        startTime: Long
    ) {
        articleFlow
            .runningFold(listOf<Article>()) { list, article -> list + article }
            .onEach { updateResults(it, startTime, completed = false) }
            .onCompletion { markLoadingCompletion(it) }
            .collect()
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
        articlesFlow.value = listOf()
        loadingStatus = IN_PROGRESS
        newLoadingEnabled = false
        cancellationEnabled = false
        currentLoadingTimeMillis = 0
    }
}
