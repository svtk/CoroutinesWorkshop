package com.kotlinconf.workshop

object WorkshopServerConfig {
    const val HOST = "127.0.0.1"
    const val PORT = 9020
    const val WORKSHOP_SERVER_URL = "http://$HOST:$PORT"
    const val WS_SERVER_URL = "ws://$HOST:$PORT"
}