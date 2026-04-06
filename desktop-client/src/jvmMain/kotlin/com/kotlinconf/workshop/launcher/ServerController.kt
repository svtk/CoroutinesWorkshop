package com.kotlinconf.workshop.launcher

import com.kotlinconf.workshop.WorkshopServerConfig.WORKSHOP_SERVER_URL
import com.kotlinconf.workshop.createWorkshopServer
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class ServerController {
    private var server = createWorkshopServer()

    suspend fun start() {
        withContext(Dispatchers.IO) {
            server.start(wait = false)
        }
        waitUntilReady()
    }

    fun stop() {
        try {
            server.stop()
        } catch (_: Exception) {
        }
    }

    private suspend fun waitUntilReady() {
        HttpClient(CIO).use { client ->
            repeat(40) { attempt ->
                val isReady = try {
                    client.get(WORKSHOP_SERVER_URL).status.isSuccess()
                } catch (exception: CancellationException) {
                    throw exception
                } catch (_: Exception) {
                    false
                }
                if (isReady) {
                    return
                }
                delay(250)
                if (attempt == 39) {
                    error("Workshop server did not become ready on $WORKSHOP_SERVER_URL")
                }
            }
        }
    }
}
