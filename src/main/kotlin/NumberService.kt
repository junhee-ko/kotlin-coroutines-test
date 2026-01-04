import kotlinx.coroutines.*

class NumberService(
    private val coroutineScope: CoroutineScope
) {

    var fetchedNumber: Int = 0
        private set

    fun fetchNumberAsync() {
        coroutineScope.launch {
            delay(5_000)
            fetchedNumber = 1
        }
    }
}
