package com.kotlinconf.workshop.tasks

import com.kotlinconf.workshop.blog.User
import com.kotlinconf.workshop.model.Article
import com.kotlinconf.workshop.model.findActiveUsers
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

fun observeActiveUsers(articleFlow: Flow<List<Article>>): Flow<Set<User>> {
    return articleFlow.map {
        it.findActiveUsers()
    }.distinctUntilChanged()
}