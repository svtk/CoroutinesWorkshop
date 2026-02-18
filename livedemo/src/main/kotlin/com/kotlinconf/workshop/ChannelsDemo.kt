package com.kotlinconf.workshop.channelsdemo

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

fun main(): Unit = runBlocking {
    // Creating a channel
    // val channel = Channel<Int>()
    // Slow consumer? Up the buffer capacity!
    // ...and provide a buffer overflow strategy!
    val channel = Channel<Int>(10)

    // Coroutine to send values to the channel
    launch {
        for (i in 1..8) {
            println("Sending $i...")
            channel.send(i)
            println("Sent $i.")
            delay(1.seconds)
        }
        channel.close() // Close the channel when done sending
    }

//    // Coroutine to receive values from the channel
//    launch {
//        // under the hood: channel.receive()
//        for (value in channel) {
//            println("A received $value")
//        }
//    }

    // Slow Consumer
    launch {
        for (value in channel) {
            println("B received $value")
            delay(2.seconds)
        }
    }


//    // Multiple consumers
//    launch {
//        for (value in channel) {
//            println("B received $value")
//        }
//    }
}