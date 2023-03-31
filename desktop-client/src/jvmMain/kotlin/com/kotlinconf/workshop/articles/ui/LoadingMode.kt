package com.kotlinconf.workshop.articles.ui

enum class LoadingMode {
    BLOCKING,
    SUSPENDING,
    CONCURRENT,
    NON_CANCELLABLE,
    WITH_PROGRESS,
    CONCURRENT_WITH_PROGRESS,
    UNSTABLE_NETWORK,
    UNSTABLE_WITH_RETRY,
}