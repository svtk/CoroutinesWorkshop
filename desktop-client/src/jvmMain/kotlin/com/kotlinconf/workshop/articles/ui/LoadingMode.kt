package com.kotlinconf.workshop.articles.ui

enum class LoadingMode {
    BLOCKING,
    SUSPENDING,
    CONCURRENT,
    NON_CANCELLABLE,
    UNSTABLE,
    WITH_PROGRESS,
    CONCURRENT_WITH_PROGRESS,
}