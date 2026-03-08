package nba

interface NbaApiClient {

    suspend fun fetchLatestGames(): List<GameResult>
}