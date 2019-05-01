package examples

import kotlinx.coroutines.*

data class Image(val name: String)

fun overlay(image1: Image, image2: Image): Image
    = Image(image1.name + "&" + image2.name)

suspend fun loadImage(name: String, timeout: Long): Image {
    log.info("Image $name started loading")
    delay(timeout)
    log.info("Image $name loaded")
    return Image(name)
}

suspend fun loadAndOverlay() = coroutineScope {
    log.info("1")
    val first = loadImageAsync("greed", 1000L)
    val second = loadImageAsync("red", 2000L)
    log.info("2")
    overlay(
        first.await().also { log.info("3") },
        second.await().also { log.info("4") })
}

private fun CoroutineScope.loadImageAsync(name: String, timeout: Long): Deferred<Image> {
    return async(Dispatchers.Default) {
        loadImage(name, timeout)
    }
}

fun main() = runBlocking<Unit> {
    println(loadAndOverlay())
}