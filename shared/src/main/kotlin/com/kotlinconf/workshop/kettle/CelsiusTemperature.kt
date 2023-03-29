package com.kotlinconf.workshop.kettle

import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

@Serializable
@JvmInline
value class CelsiusTemperature(val value: Double): Comparable<CelsiusTemperature> {
    override fun compareTo(other: CelsiusTemperature): Int =
        this.value.compareTo(other.value)

    operator fun plus(diff: Double) = CelsiusTemperature(this.value + diff)

    operator fun minus(diff: Double) = CelsiusTemperature(this.value - diff)

    override fun toString(): String {
        return "${value.roundToInt()}"
    }
}

val Double.celsius get() = CelsiusTemperature(this)

@Serializable
@JvmInline
value class FahrenheitTemperature(val value: Double) {
    override fun toString(): String {
        return "${value.roundToInt()}"
    }
}

val Double.fahrenheit get() = FahrenheitTemperature(this)

fun CelsiusTemperature.toFahrenheit() = FahrenheitTemperature(value * 1.8 + 32)