/**
 * You can edit, run, and share this code.
 * play.kotlinlang.org
 */
import kotlinx.coroutines.*
import kotlin.time.*
import kotlin.time.Duration.Companion.milliseconds

// Here, withContext falsely overrides the Job, preventing withTimeout to work correctly

val scope = CoroutineScope(Job())

fun main() = runBlocking<Unit> {
    println(measureTime {
        try {
            withTimeout(500.milliseconds) {
                val timeoutScope = this
                println(timeoutScope.coroutineContext.job.parent)
                withContext(scope.coroutineContext) {
                    val insideContextScope = this
                    // job: scope (not withTimeout)
                    println(insideContextScope.coroutineContext.job.parent)
                    delay(1000.milliseconds)
                }
            }
        }
        catch (ignored: TimeoutCancellationException) {
            // Nothing.
        }
    })
}