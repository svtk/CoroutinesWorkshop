package com.kotlinconf.workshop.articles

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