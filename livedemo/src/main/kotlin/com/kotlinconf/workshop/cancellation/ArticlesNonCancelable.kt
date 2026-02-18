package com.kotlinconf.workshop.cancellation

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Duration.Companion.milliseconds

private fun createBlogService() = 42
private fun loadArticlesNonCancelable(service: Int) {}

fun main() = runBlocking {
    val service = createBlogService()
    val job = launch {
        loadArticlesNonCancelable(service)
    }
    delay(100.milliseconds)
    job.cancel()
    delay(3.seconds) // Why do we need delay?
}