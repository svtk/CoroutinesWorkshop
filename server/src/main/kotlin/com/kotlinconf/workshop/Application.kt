package com.kotlinconf.workshop

import com.kotlinconf.workshop.WorkshopServerConfig.HOST
import com.kotlinconf.workshop.WorkshopServerConfig.PORT
import com.kotlinconf.workshop.data.Kettle
import com.kotlinconf.workshop.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.CoroutineScope

fun main() {
    createWorkshopServer().start(wait = true)
}

fun createWorkshopServer(): WorkshopServer = WorkshopServer()

class WorkshopServer {
    private val server = embeddedServer(
        Netty,
        port = PORT,
        host = HOST,
        module = Application::module,
    )

    fun start(wait: Boolean) {
        server.start(wait = wait)
    }

    fun stop() {
        server.stop(gracePeriodMillis = 1_000, timeoutMillis = 5_000)
    }
}

fun Application.module() {
    configureMonitoring()
    configureSerialization()
    configureSockets()

    configureStatusRouting()
    configureArticlesRouting()
    configureWalletRouting()
    val kettle = Kettle(CoroutineScope(coroutineContext))
    configureKettleRouting(kettle)
    configureChatRouting()
}
