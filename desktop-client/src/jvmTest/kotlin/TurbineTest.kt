import app.cash.turbine.test
import com.kotlinconf.workshop.kettle.CelsiusTemperature
import com.kotlinconf.workshop.kettle.KettlePowerState
import com.kotlinconf.workshop.kettle.celsius
import com.kotlinconf.workshop.kettle.network.KettleService
import com.kotlinconf.workshop.kettle.ui.KettleViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class TurbineTest {

    @Test
    fun `viewmodel should properly convert celsius to fahrenheit`() = runBlocking {
        assert(false)
    }
}