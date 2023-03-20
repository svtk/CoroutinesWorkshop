package com.kotlinconf.workshop

import kotlinx.serialization.Serializable


@Serializable
sealed class IssueEvent

@Serializable
class AddCommentToIssueEvent(val forIssue: IssueId, val comment: Comment) : IssueEvent()

@Serializable
class CreateIssueEvent(val issue: Issue) : IssueEvent()