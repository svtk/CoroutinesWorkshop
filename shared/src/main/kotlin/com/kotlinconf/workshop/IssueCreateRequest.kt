package com.kotlinconf.workshop

import kotlinx.serialization.Serializable

@Serializable
data class IssueCreateRequest(val title: String, val author: User)