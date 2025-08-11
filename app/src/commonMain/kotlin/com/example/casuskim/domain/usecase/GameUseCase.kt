package com.example.casuskim.domain.usecase

import com.example.casuskim.domain.model.GameSession
import com.example.casuskim.domain.model.GameResult
import com.example.casuskim.domain.model.PlayerTask
import com.example.casuskim.domain.repository.GameRepository
import com.example.casuskim.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock

class GameUseCase(
    private val gameRepository: GameRepository,
    private val taskRepository: TaskRepository
) {
    suspend fun createGameSession(session: GameSession): String {
        return gameRepository.createGameSession(session)
    }
    
    suspend fun getGameSession(id: String): GameSession? {
        return gameRepository.getGameSession(id)
    }
    
    suspend fun endGameSession(sessionId: String, endTime: Long) {
        gameRepository.endGameSession(sessionId, endTime)
    }
    
    suspend fun assignTasksToPlayers(
        sessionId: String,
        players: List<String>,
        categoryIds: List<String>,
        tasksPerPlayer: Int
    ) {
        val tasks = taskRepository.getRandomTasksByCategories(categoryIds, players.size * tasksPerPlayer)
        val playerTasks = mutableListOf<PlayerTask>()
        
        players.forEachIndexed { index, playerId ->
            val startIndex = index * tasksPerPlayer
            val endIndex = minOf(startIndex + tasksPerPlayer, tasks.size)
            
            for (i in startIndex until endIndex) {
                playerTasks.add(
                    PlayerTask(
                        playerId = playerId,
                        taskId = tasks[i].id,
                        assignedAt = Clock.System.now().toEpochMilliseconds()
                    )
                )
            }
        }
        
        playerTasks.forEach { playerTask ->
            gameRepository.assignTaskToPlayer(playerTask)
        }
    }
    
    suspend fun completeTask(playerId: String, taskId: String) {
        gameRepository.completeTask(playerId, taskId, Clock.System.now().toEpochMilliseconds())
    }
    
    suspend fun getPlayerTasks(sessionId: String): Flow<List<PlayerTask>> {
        return gameRepository.getPlayerTasks(sessionId)
    }
    
    suspend fun saveGameResult(result: GameResult) {
        gameRepository.saveGameResult(result)
    }
    
    suspend fun getGameResult(sessionId: String): GameResult? {
        return gameRepository.getGameResult(sessionId)
    }
    
    suspend fun getAllGameResults(): Flow<List<GameResult>> {
        return gameRepository.getAllGameResults()
    }
}
