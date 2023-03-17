package com.kotlinconf.workshop.db

import com.kotlinconf.workshop.Comment
import com.kotlinconf.workshop.Issue
import java.util.*

class DAOInMemoryImpl : DAOFacade {
    private val _issues =
        Collections.synchronizedList(mutableListOf<Issue>())

    private val commentsMap =
        Collections.synchronizedMap(mutableMapOf<Long, MutableList<Comment>>())

    init {
        _issues += FakeData.issues
        commentsMap += FakeData.comments
    }


    override suspend fun saveIssue(issue: Issue) {
        _issues += issue
    }

    override suspend fun saveComment(comment: Comment) {
        val comments = commentsMap.getOrPut(comment.issueId) { mutableListOf() }
        comments += comment
    }

    override val issues: List<Issue>
        get() = _issues

    override fun getComments(issueId: Long): List<Comment> {
        return commentsMap.getOrDefault(issueId, emptyList())
    }
}