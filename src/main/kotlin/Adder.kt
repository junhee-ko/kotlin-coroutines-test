import kotlinx.coroutines.delay

class Adder {

    suspend fun add(repeatTime: Int): Int {

        var result = 0

        repeat(repeatTime) {
            delay(1000L)
            result += 1
        }

        return result
    }
}
