package com.kotlinconf.workshop.tasks

import com.kotlinconf.workshop.Comment
import com.kotlinconf.workshop.User
import com.kotlinconf.workshop.data.IssueTrackerService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.runningReduce
import kotlinx.coroutines.launch

suspend fun IssueTrackerService.getCommentsByUser(user: User): List<Comment> {
    val issues = getIssues()
    val commentsByUser = issues.flatMap { issue ->
        getComments(issue.id)
            .filter { comment -> comment.author == user }
    }
    return commentsByUser
}

suspend fun IssueTrackerService.getCommentsByUserAsync(user: User): List<Comment> = coroutineScope {
    val issues = getIssues()
    val deferreds = issues.map { issue ->
        async {
            getComments(issue.id)
                .filter { comment -> comment.author == user }
        }
    }
    deferreds.awaitAll().flatten()
}

suspend fun IssueTrackerService.getCommentsByUserWithProgress(user: User): Flow<List<Comment>> {
    val issues = getIssues()
    return flow {
        for (issue in issues) {
            val comments = getComments(issue.id)
                .filter { comment -> comment.author == user }
            emit(comments)
        }
    }.runningReduce { accumulator, list ->
        accumulator + list
    }
}

suspend fun IssueTrackerService.getCommentsByUserAsyncWithProgress(user: User): Flow<List<Comment>> {
    val issues = getIssues()
    return channelFlow {
        for (issue in issues) {
            launch {
                val comments = getComments(issue.id)
                    .filter { comment -> comment.author == user }
                send(comments)
            }
        }
    }.runningReduce { accumulator, list ->
        accumulator + list
    }
}