package com.kotlinconf.workshop.issues.ui

import com.kotlinconf.workshop.AddCommentToIssueEvent
import com.kotlinconf.workshop.Comment
import com.kotlinconf.workshop.CreateIssueEvent
import com.kotlinconf.workshop.Issue
import com.kotlinconf.workshop.issues.network.IssuesService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class IssuesViewModel(val issuesService: IssuesService, parentScope: CoroutineScope) {
    val scope = CoroutineScope(parentScope.coroutineContext + SupervisorJob())
    private val _issueFlow: MutableStateFlow<List<Issue>> = MutableStateFlow(listOf())
    val issueFlow: StateFlow<List<Issue>> get() = _issueFlow

    private val _commentFlow: MutableStateFlow<List<Comment>> = MutableStateFlow(listOf())
    val commentFlow: StateFlow<List<Comment>> get() = _commentFlow

    init {
        scope.launch {
            issuesService.getEvents().collect { event ->
                when (event) {
                    is AddCommentToIssueEvent -> _commentFlow.update { oldList ->
                        oldList + event.comment
                    }

                    is CreateIssueEvent -> _issueFlow.update { oldList ->
                        oldList + event.issue
                    }
                }
            }
        }
    }
}