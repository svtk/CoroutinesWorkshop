package com.kotlinconf.workshop.kettle

import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

@Serializable
@JvmInline
value class Temperature(val value: Double) {
    override fun toString(): String {
        return "${value.roundToInt()} celsius"
    }
}

val Double.celsius get() = Temperature(this)