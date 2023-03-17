package com.kotlinconf.workshop

import com.kotlinconf.workshop.db.DAOInMemoryImpl
import com.kotlinconf.workshop.plugins.configureRouting
import com.kotlinconf.workshop.plugins.configureSerialization
import com.kotlinconf.workshop.plugins.configureSockets
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*

fun main() {
    embeddedServer(Netty, port = 9010, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

@Suppress("unused")
fun Application.module() {
    val dao = DAOInMemoryImpl()
    configureSerialization()
    configureSockets()
    configureRouting(dao)
}
