package com.kotlinconf.workshop.coroutineBuilders

import com.kotlinconf.workshop.network.WorkshopServiceImpl
import com.kotlinconf.workshop.util.log
import kotlinx.coroutines.*

val service = WorkshopServiceImpl()

fun main() = runBlocking {
    val result = service.networkCall()
    log(result)
}

fun main1() {
    val scope = GlobalScope
    scope.launch {
        val result = service.networkCall()
        log(result)
    }
}

suspend fun main2() = coroutineScope<Unit> {
    val job = launch {
        service.networkCall()
    }
    val job2 = launch {
        service.anotherNetworkCall()
    }
    job.join()
    job2.cancel()
}