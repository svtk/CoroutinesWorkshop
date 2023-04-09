package com.kotlinconf.workshop

import com.kotlinconf.workshop.WorkshopServerConfig.PORT
import com.kotlinconf.workshop.data.Kettle
import com.kotlinconf.workshop.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.CoroutineScope

fun main() {
    embeddedServer(Netty, port = PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
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
