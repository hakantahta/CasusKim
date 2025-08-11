package com.example.casuskim.domain.repository

import com.example.casuskim.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategories(): Flow<List<Category>>
    suspend fun getCategoryById(id: String): Category?
    suspend fun insertCategories(categories: List<Category>)
    suspend fun deleteAllCategories()
}
