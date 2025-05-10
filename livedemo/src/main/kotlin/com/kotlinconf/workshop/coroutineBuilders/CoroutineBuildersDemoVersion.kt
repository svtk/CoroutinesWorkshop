package com.kotlinconf.workshop.coroutineBuilders.demo

import com.kotlinconf.workshop.network.Order
import com.kotlinconf.workshop.network.WorkshopServiceImpl
import com.kotlinconf.workshop.util.log
import kotlinx.coroutines.*

val service = WorkshopServiceImpl()

// First try without suspend
// Show our log function implementation
// -Dkotlinx.coroutines.debug
// pay attention to arrow
suspend fun main0() {
    val result = service.networkCall()
    log(result)
}

// runBlocking
fun main1() {
    runBlocking {
        val result = service.networkCall()
        log(result)
    }
}

// Launch + Job
// You can use "delay" to see that orders may be different
// launch is an extension on CoroutineScope!
// There is no need for me to explicitly call .join
fun main() = runBlocking {
    val job = launch {
        log(service.networkCall())
    }
    val job2 = launch {
        log(service.anotherNetworkCall())
    }
    log("Continuing...")
    job.join()
    job2.join()
    // PUZZLER: Why the first network call isn't canceled?
//    job2.join()
//    job.cancel()
}

// Sequential vs Concurrent
fun main3() = runBlocking {
    val userId = service.login("user", "1234")
    val shopInfo = service.loadShopInfo()
    val orders = service.loadOrders(userId, shopInfo)
    println(orders)
}

// Deferred
fun main4() = runBlocking {
    log("Start")
    val userId = async {
        service.login("user", "1234")
    }
    val shopInfo = async {
        service.loadShopInfo()
    }
    val orders = service.loadOrders(userId.await(), shopInfo.await())
    log(orders)
}

// These functions also take arguments
// Change Dispatcher and CoroutineName
fun main5() = runBlocking(Dispatchers.Default) {
    log("Start")
    val userId = async(CoroutineName("login")) {
        service.login("user", "1234")
    }
    val blogInfo = async(CoroutineName("bloginfo")) {
        service.loadShopInfo()
    }
    val orders = service.loadOrders(userId.await(), blogInfo.await())
    log(orders)
}