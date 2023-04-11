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

// Whenever we start a new coroutine,
// it inherits the context
// and registers itself as a child of a parent coroutine
fun main2() = runBlocking<Unit> {
    val job = launch {
        delay(100)
    }
    println(job)
    println("Children of the coroutine: ${coroutineContext.job.children.toList()}")
}

// probably not
// how does it work with suspend fun? which context does it have
// the same output as above: suspend fun accesses the context it was called in
fun main3() = runBlocking<Unit> {
    whichContextDoIHave("grandparent")

    launch(Dispatchers.Default + CoroutineName("my")) {
        whichContextDoIHave("parent")

        launch { whichContextDoIHave("child") }
    }
}

suspend fun whichContextDoIHave(whoAmI: String) {
    println("$whoAmI: $coroutineContext")
}