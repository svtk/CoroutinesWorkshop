package com.kotlinconf.workshop

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VirtualUser {
    fun createRandomCommentEvent(issueTracker: IssueTracker): AddCommentToIssueEvent {
        val author = authors.random()
        val subject = subject.random()
        val text = listOf(
            "Wow, nice!",
            "What if $subject $action?",
            "Don't forget about the $subject.",
            "Could the $subject become relevant here?",
            "Is it possible to make the error messages more like a $subject?",
            "I think our app is trying to communicate with the $subject.",
            "Can we make the loading spinner do the $subject dance?",
            "My code is so beautiful, it deserves a $subject.",
        ).random()
        return AddCommentToIssueEvent(issueTracker.allIssues().random().id, Comment(User(author), text))
    }

    val authors = listOf("User 1", "seb", "marie", "ysl", "fred")

    val subject = listOf(
        "washing machine",
        "cafeteria",
        "bicycle",
        "rubberduck",
        "office plant",
        "air freshener",
        "soy sauce",
        "cup of coffee",
        "friendly neighbourhood cat"
    )
    val action = listOf(
        "has a timeout",
        "was unexpectedly found",
        "needs to be refactored",
        "should not have access",
        "has been rebranded",
        "needs attention",
        "needs to be watered"
    )

    fun createRandomIssue(issueTracker: IssueTracker) {
        issueTracker.addIssue(
            User(authors.random()),
            "${subject.random()} ${action.random()}"
        )
    }

    fun beginPosting(coroutineScope: CoroutineScope, issueTracker: IssueTracker) {
        coroutineScope.launch {
            while (true) {
                delay(1000)
                val randomCommentEvent = createRandomCommentEvent(issueTracker)
                issueTracker.addComment(randomCommentEvent.forIssue, randomCommentEvent.comment)
            }
        }
    }
}