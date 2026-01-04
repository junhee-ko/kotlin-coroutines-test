import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NumberService {

    var fetchedNumber: Int = 0
        private set

    fun fetchNumberAsync() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(5_000)
            fetchedNumber = 1
        }
    }
}
