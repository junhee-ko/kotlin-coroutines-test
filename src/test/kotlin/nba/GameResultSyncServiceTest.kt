package nba

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
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

    @OptIn(ExperimentalCoroutinesApi::class)
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
        val testScope = TestScope()

        val gameResultSyncService = GameResultSyncService(
            apiClient = fakeApiClient,
            repository = fakeRepository,
            coroutineScope = testScope,
        )

        // when
        gameResultSyncService.sync()
        testScope.advanceUntilIdle()

        // then
        assertEquals(2, fakeRepository.savedGames.size)
    }

    @Test
    fun `scope 를 cancel 하면 진행 중인 코루틴이 즉시 중단된다`() {
        // given
        val fakeApiClient = FakeNbaApiClient(
            games = listOf(
                GameResult(homeTeam = "Lakers", awayTeam = "Celtics", homeScore = 112, awayScore = 108),
                GameResult(homeTeam = "Bulls", awayTeam = "Warriors", homeScore = 99, awayScore = 104)
            ),
            delayMillis = 5_000L
        )
        val fakeRepository = FakeGameResultRepository()
        val coroutineScope = TestScope()

        val gameResultSyncService = GameResultSyncService(
            apiClient = fakeApiClient,
            repository = fakeRepository,
            coroutineScope = coroutineScope
        )

        // when
        gameResultSyncService.sync()
        coroutineScope.cancel()

        // then
        assertEquals(0, fakeRepository.savedGames.size)
    }
}