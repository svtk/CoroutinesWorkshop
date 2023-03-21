package com.kotlinconf.workshop

import kotlinx.serialization.Serializable

@Serializable
data class Issue(val id: IssueId, val author: User, val title: String, val status: IssueStatus)

@JvmInline
@Serializable
value class IssueId(val id: Int)