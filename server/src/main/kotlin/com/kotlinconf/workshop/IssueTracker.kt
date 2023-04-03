package com.kotlinconf.workshop

import io.ktor.server.application.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow


fun Application.setupIssueTracker(): IssueTracker {
    val issueTracker = IssueTracker()
    val vUser = GlobalVirtualUser.instance
    repeat(20) {
        vUser.createRandomIssue(issueTracker)
    }
    repeat(200) {
        vUser.createRandomCommentEvent(issueTracker)
    }
    return issueTracker
}


class IssueTracker {
    private val comments = mutableMapOf<IssueId, MutableList<Comment>>(
        IssueId(0) to mutableListOf(
            Comment(User("sebi_io"), "Then what am I looking at? 🤨")
        )
    )

    private val issues = mutableListOf<Issue>(
        Issue(
            IssueId(0),
            User("seb"),
            "Implement issue tracker",
            IssueStatus.WONTFIX,
        )
    )

    fun allIssues(): List<Issue> = issues

    fun commentsForId(issueId: IssueId): List<Comment> {
        return comments[issueId] ?: listOf()
    }


    // For now, this just provides an "ad hoc" view of what's going on without any guarantees about completeness of the log you receive
    private val _issueEvents =
        MutableSharedFlow<IssueEvent>(onBufferOverflow = BufferOverflow.DROP_OLDEST, replay = 1)
    val issueEvents = _issueEvents.asSharedFlow()

    fun addComment(issueId: IssueId, comment: Comment) {
        val issue = allIssues().firstOrNull { it.id == issueId } ?: return
        val allComments = comments.getOrPut(issue.id) { mutableListOf() }
        allComments += comment
        comments[issueId] = allComments
        _issueEvents.tryEmit(AddCommentToIssueEvent(issueId, comment))
        println("Added $comment")
    }

    fun issueForId(id: IssueId): Issue? {
        return issues.find { it.id == id }
    }

    fun addIssue(author: User, title: String): Issue {
        val newId = IssueId(allIssues().maxOf { it.id.id } + 1)
        val newIssue = Issue(id = newId, author = author, title = title, status = IssueStatus.OPEN)
        issues.add(newIssue)
        _issueEvents.tryEmit(CreateIssueEvent(newIssue))
        return newIssue
    }
}
