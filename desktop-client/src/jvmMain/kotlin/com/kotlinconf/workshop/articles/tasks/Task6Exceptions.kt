package com.kotlinconf.workshop.articles.tasks

import com.kotlinconf.workshop.articles.model.Article
import com.kotlinconf.workshop.articles.network.BlogService
import com.kotlinconf.workshop.articles.network.NetworkException
import kotlinx.coroutines.flow.*

fun observeArticlesUnstable(service: BlogService): Flow<Article> = flow {
    throw NetworkException("Loading articles was unsuccessful.")
}