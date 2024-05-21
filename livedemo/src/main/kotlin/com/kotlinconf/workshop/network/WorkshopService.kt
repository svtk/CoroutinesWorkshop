package com.kotlinconf.workshop.network

import com.kotlinconf.workshop.blog.ArticleInfo
import com.kotlinconf.workshop.util.log
import kotlinx.coroutines.delay

// TODO network
data class UserID(val id: Long)
data class UserData(val id: UserID, val name: String)

data class ShopInfo(val title: String)

data class Order(val userId: UserID, val description: String)

interface WorkshopService {

    suspend fun networkCall(): String

    suspend fun anotherNetworkCall(): String

    suspend fun login(username: String, password: String): UserID
    suspend fun loadShopInfo(): ShopInfo

    suspend fun loadShopInfoFailing(): ShopInfo
    suspend fun loadOrders(userId: UserID, shopInfo: ShopInfo): List<Order>
}

class WorkshopServiceImpl : WorkshopService {

    override suspend fun networkCall(): String {
        log("Sending network request")
        delay(1000)
        log("Receiving result")
        return "Result"
    }

    override suspend fun anotherNetworkCall(): String {
        log("Sending another network request")
        delay(1500)
        log("Receiving another result")
        return "Another Result"
    }

    override suspend fun login(username: String, password: String): UserID {
        log("Login $username starts")
        delay(1000)
        return UserID(0)
            .also { log("User $username successfully logged!") }
    }

    override suspend fun loadShopInfo(): ShopInfo {
        log("Loading shop info starts")
        delay(1000)
        return ShopInfo("Kotlin")
            .also { log("Loading shop info completes: $it") }
    }

    override suspend fun loadShopInfoFailing(): ShopInfo {
        log("Loading shop info starts")
        delay(500)
        throw Exception("Loading error")
    }

    override suspend fun loadOrders(userId: UserID, shopInfo: ShopInfo): List<Order> {
        log("Loading orders starts")
        delay(1000)
        return listOf<Order>()
            .also { log("Loading orders completes: loaded ${it.size} orders") }
    }
}