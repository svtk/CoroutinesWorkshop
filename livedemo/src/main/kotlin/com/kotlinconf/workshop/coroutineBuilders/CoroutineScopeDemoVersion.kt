@file:Suppress("DuplicatedCode")
package com.kotlinconf.workshop.coroutineBuilders.coroutineScopeDemo.demo

import com.kotlinconf.workshop.coroutineBuilders.service
import com.kotlinconf.workshop.network.Order
import com.kotlinconf.workshop.util.log
import kotlinx.coroutines.*

// Extract loadOrders into a separate function
fun main() = runBlocking {
    val userId = async {
        service.login("user", "1234")
    }
    val blogInfo = async {
        service.loadShopInfo()
    }
    log(service.loadOrders(userId.await(), blogInfo.await()))
}

fun loadOrders(): List<Order> {
    TODO()
}

// Result
fun main1() = runBlocking {
    log(loadOrders1())
}

suspend fun loadOrders1(): List<Order> = coroutineScope {
    val userId = async {
        service.login("user", "1234")
    }
    val blogInfo = async {
        service.loadShopInfo()
    }
    service.loadOrders(userId.await(), blogInfo.await())
}