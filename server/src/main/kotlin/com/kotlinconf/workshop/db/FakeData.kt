package com.kotlinconf.workshop.db

import com.kotlinconf.workshop.Comment
import com.kotlinconf.workshop.Issue
import com.kotlinconf.workshop.User

object FakeData {
    private const val numberOfIssues = 10L

    val issues = buildList {
        for (i in 0L..numberOfIssues) {
            add(Issue(i, "Issue $i"))
        }
    }

    val users = buildList {
        for (i in 1..10) {
            add(User("User $i"))
        }
    }

    val comments = buildCommentsMap()

    private var commentId = 0L
    private fun buildCommentsMap(): MutableMap<Long, MutableList<Comment>> {
        val comments = mutableMapOf<Long, MutableList<Comment>>()
        repeat(100) {
            val issueId = issues.random().id
            val list = comments.getOrPut(issueId) { mutableListOf() }
            list += Comment(
                commentId++, issueId, users.random(),
                "Comment $commentId", 0L
            )
        }
        return comments
    }
}