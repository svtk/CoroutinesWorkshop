package com.kotlinconf.workshop.kettle.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// Task: Implement a custom averageOfLast flow operator
fun Flow<Double>.averageOfLast(n: Int): Flow<Double> = flow {
    // initial code: empty
    val deque = ArrayDeque<Double>(n)
    collect {
        if (deque.size > n) {
            deque.removeFirst()
        }
        deque.addLast(it)
        emit(deque.average())
    }
}