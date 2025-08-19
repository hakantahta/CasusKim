package com.example.casuskim.domain.repository

import com.example.casuskim.domain.model.GameSession
import com.example.casuskim.domain.model.GameResult
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    suspend fun createGameSession(session: GameSession): String
    suspend fun getGameSession(id: String): GameSession?
    suspend fun updateGameSession(session: GameSession)
    suspend fun endGameSession(sessionId: String, endTime: Long)
    
    suspend fun assignSpyAndWords(sessionId: String, players: List<String>, word: String)
    suspend fun getCurrentPlayer(sessionId: String): String?
    suspend fun moveToNextPlayer(sessionId: String)
    
    suspend fun saveGameResult(result: GameResult)
    suspend fun getGameResult(sessionId: String): GameResult?
    suspend fun getAllGameResults(): Flow<List<GameResult>>
}
