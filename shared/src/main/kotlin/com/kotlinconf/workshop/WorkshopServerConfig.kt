package com.kotlinconf.workshop

object WorkshopServerConfig {
    const val HOST = "http://0.0.0.0"
    const val PORT = 9020
    const val WORKSHOP_SERVER_URL = "$HOST:$PORT"

    // ?
    val articlesEndpoint = "$WORKSHOP_SERVER_URL/articles"
    fun commentsEndpoint(id: Int) = "$WORKSHOP_SERVER_URL/articles/$id/comments"
    fun commentsUnstableEndpoint(id: Int) = commentsEndpoint(id) + "/?failure=0.5"
}