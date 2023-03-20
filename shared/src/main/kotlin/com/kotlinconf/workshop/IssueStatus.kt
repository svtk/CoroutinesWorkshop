package com.kotlinconf.workshop

import kotlinx.serialization.Serializable

@Serializable
enum class IssueStatus {
    OPEN,
    FIXED,
    WONTFIX
}