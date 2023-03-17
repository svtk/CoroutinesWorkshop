package com.kotlinconf.workshop.data

import com.kotlinconf.workshop.Comment
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import com.kotlinconf.workshop.Issue
import com.kotlinconf.workshop.util.log

class IssueTrackerService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    private val host = "http://0.0.0.0:9010/"
    private val issuesEndpoint = "$host/issues"
    private fun commentsEndpoint(issueId: Long) = "$host/issue/$issueId/comments"

    suspend fun getIssues(): List<Issue> {
        log("Loading issues")
        return client.get(issuesEndpoint).body()
    }

    suspend fun getComments(issueId: Long): List<Comment> {
        log("Loading comments for issue [$issueId]")
        return client.get(commentsEndpoint(issueId)).body()
    }
}