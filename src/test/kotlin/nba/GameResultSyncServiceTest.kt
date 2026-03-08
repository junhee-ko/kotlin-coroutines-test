package nba

import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GameResultSyncServiceTest {

    class FakeNbaApiClient(
        private val games: List<GameResult> = emptyList(),
        private val delayMillis: Long = 0L,
    ) : NbaApiClient {

        override suspend fun fetchLatestGames(): List<GameResult> {
            delay(delayMillis) // Simulate network latency
            return games
        }
    }

    class FakeGameResultRepository : GameResultRepository {

        val savedGames = mutableListOf<GameResult>()

        override suspend fun saveAll(games: List<GameResult>) {
            savedGames.addAll(games)
        }
    }

    @Test
    fun `경기 결과를 저장한다`() {
        // given
        val fakeApiClient = FakeNbaApiClient(
            games = listOf(
                GameResult(homeTeam = "Lakers", awayTeam = "Celtics", homeScore = 112, awayScore = 108),
                GameResult(homeTeam = "Bulls", awayTeam = "Warriors", homeScore = 99, awayScore = 104)
            )
        )
        val fakeRepository = FakeGameResultRepository()
        val standardTestDispatcher = StandardTestDispatcher()

        val gameResultSyncService = GameResultSyncService(
            apiClient = fakeApiClient,
            repository = fakeRepository,
            dispatcher = standardTestDispatcher,
        )

        // when
        gameResultSyncService.sync()
        standardTestDispatcher.scheduler.advanceUntilIdle()

        // then
        assertEquals(2, fakeRepository.savedGames.size)
    }
}