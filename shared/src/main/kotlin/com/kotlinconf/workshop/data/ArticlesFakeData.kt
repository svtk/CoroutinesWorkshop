package com.kotlinconf.workshop.data

import com.kotlinconf.workshop.blog.ArticleInfo
import com.kotlinconf.workshop.blog.Comment
import com.kotlinconf.workshop.blog.User
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

object ArticlesFakeData {
    private val users = mutableSetOf<User>()
    private val articleInfoList = mutableListOf<ArticleInfo>()
    private val commentsMap = mutableMapOf<Int, List<Comment>>()
    private val delays = mutableMapOf<Int, Duration>()

    fun getArticles(): List<ArticleInfo> {
        return articleInfoList
    }
    fun getComments(articleId: Int): List<Comment> {
        return commentsMap.getValue(articleId)
    }
    fun getDelay(articleId: Int): Duration {
        return delays.getValue(articleId)
    }

    private fun user(name: String): User {
        val existingUser = users.find { it.name == name }
        if (existingUser != null) return existingUser

        val id = users.maxByOrNull { it.id }?.id ?: 0
        val newUser = User(id + 1, name)
        users += newUser
        return newUser
    }

    private fun addArticle(
        delay: Duration,
        author: String,
        title: String,
        commenters: List<String>
    ) {
        val id = commentsMap.keys.size + 1
        val articleInfo = ArticleInfo(id, user(author), title)
        articleInfoList += articleInfo
        val comments = commenters.mapIndexed { index, commenter ->
            Comment(user(commenter), "Comment $index")
        }
        commentsMap[id] = comments
        delays[id] = delay
    }

    init {
        addArticle(1.seconds, "Andrey", "Kotlin 1.8.0 Released",
            listOf("Emma", "Yara", "Milan", "James", "Finn", "Yara", "Milan"))
        addArticle(1.5.seconds, "Ekaterina", "Shout-Out to Kotlin Multiplatform Content Creators!",
            listOf("James", "Yara", "Milan", "James", "Milan", "James", "Milan", "James", "Yara"))
        addArticle(0.75.seconds, "Ksenia", "Apply to Google Summer of Code – Kotlin Projects Available",
            listOf("Luca", "James", "Luca", "James"))
        addArticle(0.25.seconds, "Roman", "The K2 Compiler is going stable in Kotlin 2.0",
            listOf("Emma", "James"))
        addArticle(2.seconds, "Ekaterina", "Update Regarding Kotlin/Native Targets",
            listOf("Milan", "Levi", "Sem", "Levi", "Sem", "James"))
        addArticle(1.75.seconds, "Denis", "We Are Improving Library Authors’ Experience!",
            listOf("James", "Yara"))
        addArticle(0.5.seconds, "Ksenia", "Advent of Code in Kotlin",
            listOf("Luca"))
        addArticle(1.25.seconds, "Ekaterina", "We Are Looking For EAP Champions!",
            listOf("Emma", "Luca", "Milan"))
    }
}