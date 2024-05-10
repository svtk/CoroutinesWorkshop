package com.kotlinconf.workshop.puzzlers

import kotlinx.coroutines.*
import kotlinx.coroutines.time.withTimeout
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

suspend fun main() {
    withContext(MyIdentifierElement("IDENT-123")) {
        handle()
    }
}

suspend fun handle() {
    println(coroutineContext[MyIdentifierElement])
}


data class MyIdentifierElement(
    private val text: String,
) : CoroutineContext.Element {
    companion object Key : CoroutineContext.Key<MyIdentifierElement>
    override val key = MyIdentifierElement
}
