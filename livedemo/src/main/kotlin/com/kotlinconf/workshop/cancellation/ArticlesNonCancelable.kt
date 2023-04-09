package com.kotlinconf.workshop.cancellation

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private fun createBlogService() = 42
private fun loadArticlesNonCancelable(service: Int) {}

fun main() = runBlocking {
    val service = createBlogService()
    val job = launch {
        loadArticlesNonCancelable(service)
    }
    delay(100)
    job.cancel()
    delay(3000) // Why do we need delay?
}