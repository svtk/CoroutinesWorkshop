package com.kotlinconf.workshop.kettle.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// Task: Implement a custom averageOfLast flow operator
fun Flow<Double>.averageOfLast(n: Int): Flow<Double> = flow {
    // initial code: empty
    val lastElements = ArrayDeque<Double>()
    collect { value ->
        lastElements.add(value)
        if (lastElements.size > n) {
            lastElements.removeFirst()
        }
        emit(lastElements.average())
    }
}
