package nba

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GameResultSyncService(
    private val apiClient: NbaApiClient,
    private val repository: GameResultRepository,
    private val coroutineScope: CoroutineScope
) {

    fun sync() {
        coroutineScope.launch {
            val games = apiClient.fetchLatestGames()
            repository.saveAll(games)
        }
    }
}