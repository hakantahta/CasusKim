package com.example.casuskim.domain.usecase

import com.example.casuskim.domain.model.Word
import com.example.casuskim.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow

class GetWordsUseCase(
    private val wordRepository: WordRepository
) {
    fun getWordsByCategory(categoryId: String): Flow<List<Word>> {
        return wordRepository.getWordsByCategory(categoryId)
    }
    
    suspend fun getRandomWordByCategory(categoryId: String): Word? {
        return wordRepository.getRandomWordByCategory(categoryId)
    }
}
