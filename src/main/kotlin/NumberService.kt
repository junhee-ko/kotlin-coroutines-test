import kotlinx.coroutines.*

class NumberService(
    private val coroutineDispatcher: CoroutineDispatcher
) {

    var fetchedNumber: Int = 0
        private set

    fun fetchNumberAsync() {
        CoroutineScope(coroutineDispatcher).launch {
            delay(5_000)
            fetchedNumber = 1
        }
    }
}
