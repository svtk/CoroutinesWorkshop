package com.kotlinconf.workshop.context

import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

// coroutine context is part of scope
// navigate to coroutine context
fun main0() = runBlocking<Unit> {
    println(coroutineContext)

    launch {
        println(coroutineContext)
    }
}

// context is an immutable set of elements such as:
// (slides?)

// inheriting context from parent
// adding more elements to it
// these elements work for the whole hierarchy
// navigate to plus?
fun main1() = runBlocking<Unit> {
    println("grandparent: $coroutineContext")

    launch(Dispatchers.Default + CoroutineName("my")) {
        println("parent: $coroutineContext")

        launch { println("child: $coroutineContext") }
    }
}

// how does it work with suspend fun? which context does it have
// the same output as above: suspend fun accesses the context it was called in
fun main2() = runBlocking<Unit> {
    whichContextDoIHave("grandparent")

    launch(Dispatchers.Default + CoroutineName("my")) {
        whichContextDoIHave("parent")

        launch { whichContextDoIHave("child") }
    }
}

suspend fun whichContextDoIHave(whoAmI: String) {
    println("$whoAmI: $coroutineContext")
}

// how exactly does it work with loadArticlesConcurrently? Why is it cancellable?
// TODO try to visualize this hierarchy in slides
fun main3() = runBlocking<Unit> {
    launch {
        loadArticlesConcurrently()
    }
    println("Children of the main coroutine: ${coroutineContext.job.children.toList()}")

}

suspend fun loadArticlesConcurrently() {
    val parentContext = coroutineContext
    delay(100)
    println("Launch context: ${parentContext.job}")
    coroutineScope {
        delay(200)
        println("Children of launch job: ${parentContext.job.children.toList()}")
        println("Coroutine scope context: ${coroutineContext.job}")
    }
}
