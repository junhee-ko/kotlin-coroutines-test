import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AdderTest {

//    /*
//    * Suspend function 'suspend fun add(repeatTime: Int): Int' should be called only from a coroutine or another suspend function.
//    * */
//    @Test
//    fun `repeatTime이 100이면 결과는 100이다`() {
//        // given
//        val adder = Adder()
//
//        // when
//        val actual = adder.add(repeatTime = 5)
//
//        // then
//        assertEquals(100, actual)
//    }

    @Test
    fun `Step 1 - runBlocking 기본 사용`() = runBlocking {
        // given
        val adder = Adder()

        // when
        val actual = adder.add(repeatTime = 100)

        // then
        assertEquals(100, actual)
    }


    @Test
    fun `Step 2 - TestCoroutineScheduler와 StandardTestDispatcher 조합`() = runBlocking {
        // given
        val scheduler = TestCoroutineScheduler()
        val dispatcher = StandardTestDispatcher(scheduler)
        val adder = Adder()

        // when
        val deferred = async(dispatcher) {
            adder.add(100)
        }
        scheduler.advanceUntilIdle()

        // then
        assertEquals(100, deferred.getCompleted())
    }


    @Test
    fun `Step 3 - StandardTestDispatcher 단독 사용`() = runBlocking {
        // given
        val dispatcher = StandardTestDispatcher()
        val adder = Adder()

        // when
        val deferred = async(dispatcher) {
            adder.add(100)
        }
        dispatcher.scheduler.advanceUntilIdle()

        // then
        assertEquals(100, deferred.getCompleted())
    }


    @Test
    fun `Step 4 - TestScope 사용`() {
        // given
        val scope = TestScope()
        val adder = Adder()

        // when
        val deferred = scope.async {
            adder.add(100)
        }
        scope.testScheduler.advanceUntilIdle()

        // then
        assertEquals(100, deferred.getCompleted())
    }


    @Test
    fun `Step 5 - runTest 사용 (최종 권장 방식)`() = runTest {
        // given
        val adder = Adder()

        // when
        val actual = adder.add(100)

        // then
        assertEquals(100, actual)
    }
}