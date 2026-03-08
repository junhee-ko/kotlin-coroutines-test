package nba

interface GameResultRepository {

    suspend fun saveAll(games: List<GameResult>)
}