package examples

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {
    val channel = Channel<Int>()
    log.info("start")
    launch {
        for (x in 1..5) {
            delay(100)
            log.info("sending $x")
            channel.send(x)
        }
    }
    launch {
        repeat(5) {
            log.info("receiving ${channel.receive()}")
        }
        log.info("Done!")
    }
}