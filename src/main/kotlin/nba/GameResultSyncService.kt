package nba

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameResultSyncService(
    private val apiClient: NbaApiClient,
    private val repository: GameResultRepository
) {

    fun sync() {
        CoroutineScope(Dispatchers.IO).launch {
            val games = apiClient.fetchLatestGames()
            repository.saveAll(games)
        }
    }
}