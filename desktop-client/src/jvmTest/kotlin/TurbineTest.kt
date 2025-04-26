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

class FakeKettleService : KettleService {
    override suspend fun switchOn() {
        TODO("Not yet implemented")
    }

    override suspend fun switchOff() {
        TODO("Not yet implemented")
    }

    override fun observeTemperature(): Flow<CelsiusTemperature> {
        return flowOf(20.0.celsius, 25.0.celsius, 100.0.celsius)
    }

    override fun observeKettlePowerState(): Flow<KettlePowerState> {
        return flowOf(KettlePowerState.ON)
    }
}

class TurbineTest {

    @Test
    fun `viewmodel should properly convert celsius to fahrenheit`() = runBlocking {
        // initial code: just the runBlocking
        val vm = KettleViewModel(FakeKettleService(), this)
        vm.fahrenheitTemperature.test {
            assertEquals(68.0, awaitItem()!!.value, 0.05)
            assertEquals(77.0, awaitItem()!!.value, 0.05)
            assertEquals(212.0, awaitItem()!!.value, 0.05)
        }
    }
}