package com.kotlinconf.workshop.coroutineBuilders.newScopeWithEmptyContext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

fun main() {

    CoroutineScope(EmptyCoroutineContext).launch {
        // ...
    }


}