package examples

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

// Add the following JVM option to run configuration:
//-Dkotlinx.coroutines.debug

fun main() = runBlocking(Dispatchers.Default) {
    log("start")
    val aFuture = async {
        delay(3000)
        log("1")
        "a"
    }
    val bFuture = async {
        delay(2000)
        log("2")
        log(aFuture.await())
        "b"
    }
    val cFuture = async {
        delay(1000)
        log("3")
        log(bFuture.await())
        "c"
    }
    log(cFuture.await())
}