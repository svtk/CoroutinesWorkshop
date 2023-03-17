package com.kotlinconf.workshop.db

import com.kotlinconf.workshop.Comment
import com.kotlinconf.workshop.Issue


interface DAOFacade {

    suspend fun saveIssue(issue: Issue)

    suspend fun saveComment(comment: Comment)

    val issues: List<Issue>

    fun getComments(issueId: Long): List<Comment>
}