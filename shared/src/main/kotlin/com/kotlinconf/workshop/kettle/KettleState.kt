package com.kotlinconf.workshop.kettle

import kotlinx.serialization.Serializable

@Serializable
enum class KettleState {
    ON, OFF
}