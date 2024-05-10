package com.kotlinconf.workshop.puzzlers

import com.kotlinconf.workshop.util.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// https://github.com/Kotlin/kotlinx.coroutines/blob/bea7739faae91ccfe756973f4cb2d34c4d524321/kotlinx-coroutines-core/common/src/CoroutineDispatcher.kt#L100

fun main() {
    val disp = Dispatchers.IO.limitedParallelism(1)
    var x = 0
    runBlocking {
        repeat(10000) {
            launch(disp) {
               log(it.toString())
                x++
            }
        }
    }
    println(x) // limiting parallelism to 1 ensures that the increments happen sequential and race-free.
}