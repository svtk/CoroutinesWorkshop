import app.cash.turbine.test
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class LiveDemoTest {
    val letters = flow {
        emit("A")
        emit("B")
    }

    @Test
    fun `test our first flow`() = runBlocking {
        letters.test {
            assertEquals("A", awaitItem())
            assertEquals("B", awaitItem())
            awaitComplete()
        }
    }
}