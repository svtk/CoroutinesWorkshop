@file:Suppress("DuplicatedCode")

package com.kotlinconf.workshop.coroutineBuilders.coroutineScopeDemo

import com.kotlinconf.workshop.coroutineBuilders.service
import com.kotlinconf.workshop.network.Order
import com.kotlinconf.workshop.util.log
import kotlinx.coroutines.*

// Extract loadOrders into a separate function
fun main() = runBlocking {
    val orders = coroutineScope {
        val userId = async {
            service.login("user", "1234")
        }
        val blogInfo = async {
            service.loadShopInfo()
        }
        service.loadOrders(userId.await(), blogInfo.await())
    }
    log(orders)
}

fun loadOrders(): List<Order> {
    TODO()
}

