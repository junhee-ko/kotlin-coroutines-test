import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class NumberServiceTest{

    @Test
    fun wrong_test() {
        // given
        val coroutineDispatcher = Dispatchers.IO
        val service = NumberService(coroutineDispatcher)

        // when
        service.fetchNumberAsync()

        Thread.sleep(6_000)

        // then
        assertEquals(1, service.fetchedNumber)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun use_scheduler() {
        // given
        val scheduler = TestCoroutineScheduler()
        val dispatcher = StandardTestDispatcher(scheduler)

        val service = NumberService(dispatcher)

        // when
        service.fetchNumberAsync()

        scheduler.advanceTimeBy(5000)
        scheduler.runCurrent()
//        scheduler.advanceUntilIdle()

        // then
        assertEquals(1, service.fetchedNumber)
    }
}