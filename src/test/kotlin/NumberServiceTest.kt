import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class NumberServiceTest{

    @Test
    fun wrong_test() {
        // given
        val service = NumberService()

        // when
        service.fetchNumberAsync()

        Thread.sleep(6_000)

        // then
        assertEquals(1, service.fetchedNumber)
    }
}