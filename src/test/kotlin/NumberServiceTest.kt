import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class NumberServiceTest{

    @Test
    fun wrong_test() {
        // given
        val dispatcher = Dispatchers.IO
        val scope = CoroutineScope(dispatcher)

        val service = NumberService(scope)

        // when
        service.fetchNumberAsync()

        Thread.sleep(6_000)

        // then
        assertEquals(1, service.fetchedNumber)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun use_TestCoroutineScheduler() {
        // given
        val scheduler = TestCoroutineScheduler()
        val dispatcher = StandardTestDispatcher(scheduler)
        val scope = CoroutineScope(dispatcher)

        val service = NumberService(scope)

        // when
        service.fetchNumberAsync()

        scheduler.advanceTimeBy(5000)
        scheduler.runCurrent()
//        scheduler.advanceUntilIdle()

        // then
        assertEquals(1, service.fetchedNumber)
    }

    @Test
    fun use_StandardTestDispatcher() {
        // given
        val dispatcher = StandardTestDispatcher()
        val scope = CoroutineScope(dispatcher)

        val service = NumberService(scope)

        // when
        service.fetchNumberAsync()

        dispatcher.scheduler.advanceUntilIdle()

        // then
        assertEquals(1, service.fetchedNumber)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun use_TestScope() {
        val dispatcher = StandardTestDispatcher()
        val scope = TestScope(dispatcher)

        val service = NumberService(scope)

        service.fetchNumberAsync()

        scope.advanceUntilIdle()

        assertEquals(1, service.fetchedNumber)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun use_runTest() = runTest {
        // given
        val service = NumberService(this)

        // when
        service.fetchNumberAsync()

        advanceUntilIdle()

        // then
        assertEquals(1, service.fetchedNumber)
    }
}