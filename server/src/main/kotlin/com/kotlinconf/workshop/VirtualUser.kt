package com.kotlinconf.workshop

import com.kotlinconf.workshop.util.log
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.random.Random

object GlobalVirtualUser {
    val instance = VirtualUser()
}

class VirtualUser {
    fun createRandomCommentEvent(issueTracker: IssueTracker): AddCommentToIssueEvent {
        val author = authors.random()
        val subject = subject.random()
        val text = listOf(
            "Wow, nice!",
            "What if $subject ${action.random()}?",
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

    var postingCommentJob: Job? = null
    var postingIssueJob: Job? = null
    val m = Mutex()
    suspend fun beginPosting(coroutineScope: CoroutineScope, issueTracker: IssueTracker) {
        m.withLock {
            if (postingCommentJob?.isActive == true && postingIssueJob?.isActive == true) {
                log("Already virtually posting.")
                return
            }
            log("Beginning to post.")
            postingCommentJob?.cancelAndJoin()
            postingIssueJob?.cancelAndJoin()
            postingCommentJob = coroutineScope.launch {
                while (true) {
                    delay(Random.nextLong(200, 3000))
                    val randomCommentEvent = createRandomCommentEvent(issueTracker)
                    issueTracker.addComment(randomCommentEvent.forIssue, randomCommentEvent.comment)
                }
            }
            postingIssueJob = coroutineScope.launch {
                while (true) {
                    delay(Random.nextLong(1800, 5000))
                    createRandomIssue(issueTracker)
                }
            }
        }
    }
}