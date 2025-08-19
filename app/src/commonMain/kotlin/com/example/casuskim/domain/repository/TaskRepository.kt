package com.example.casuskim.domain.repository

import com.example.casuskim.domain.model.Word
import kotlinx.coroutines.flow.Flow

interface WordRepository {
    fun getAllWords(): Flow<List<Word>>
    fun getWordsByCategory(categoryId: String): Flow<List<Word>>
    suspend fun getWordById(id: String): Word?
    suspend fun insertWords(words: List<Word>)
    suspend fun deleteAllWords()
    suspend fun getRandomWordByCategory(categoryId: String): Word?
}
