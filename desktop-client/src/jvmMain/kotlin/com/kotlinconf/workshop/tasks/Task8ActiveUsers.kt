package com.kotlinconf.workshop.tasks

import com.kotlinconf.workshop.blog.User
import com.kotlinconf.workshop.articles.Article
import com.kotlinconf.workshop.articles.findActiveUsers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

fun observeActiveUsers(articleFlow: Flow<List<Article>>): Flow<Set<User>> {
    return articleFlow.map {
        it.findActiveUsers()
    }.distinctUntilChanged()
}