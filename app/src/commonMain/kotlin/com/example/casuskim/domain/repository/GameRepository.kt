package com.example.casuskim.domain.repository

import com.example.casuskim.domain.model.GameSession
import com.example.casuskim.domain.model.GameResult
import com.example.casuskim.domain.model.PlayerTask
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    suspend fun createGameSession(session: GameSession): String
    suspend fun getGameSession(id: String): GameSession?
    suspend fun updateGameSession(session: GameSession)
    suspend fun endGameSession(sessionId: String, endTime: Long)
    
    suspend fun assignTaskToPlayer(playerTask: PlayerTask)
    suspend fun completeTask(playerId: String, taskId: String, completedAt: Long)
    suspend fun getPlayerTasks(sessionId: String): Flow<List<PlayerTask>>
    
    suspend fun saveGameResult(result: GameResult)
    suspend fun getGameResult(sessionId: String): GameResult?
    suspend fun getAllGameResults(): Flow<List<GameResult>>
}
