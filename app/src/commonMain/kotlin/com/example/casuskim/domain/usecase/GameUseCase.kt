package com.example.casuskim.domain.usecase

import com.example.casuskim.domain.model.GameSession
import com.example.casuskim.domain.model.GameResult
import com.example.casuskim.domain.model.Player
import com.example.casuskim.domain.repository.GameRepository
import com.example.casuskim.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlin.random.Random

class GameUseCase(
    private val gameRepository: GameRepository,
    private val wordRepository: WordRepository
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
    
    suspend fun assignSpyAndWords(
        sessionId: String,
        players: List<Player>,
        categoryId: String
    ) {
        val word = wordRepository.getRandomWordByCategory(categoryId)
        if (word != null) {
            // Rastgele bir oyuncuyu casus yap
            val spyIndex = Random.nextInt(players.size)
            val updatedPlayers = players.mapIndexed { index, player ->
                player.copy(
                    isSpy = index == spyIndex,
                    assignedWord = if (index == spyIndex) "CASUS" else word.word
                )
            }
            
            // Oyuncuları güncelle
            gameRepository.assignSpyAndWords(sessionId, updatedPlayers.map { it.id }, word.word)
        }
    }
    
    suspend fun getCurrentPlayer(sessionId: String): String? {
        return gameRepository.getCurrentPlayer(sessionId)
    }
    
    suspend fun moveToNextPlayer(sessionId: String) {
        gameRepository.moveToNextPlayer(sessionId)
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
