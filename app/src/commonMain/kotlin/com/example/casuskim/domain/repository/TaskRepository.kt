package com.example.casuskim.domain.repository

import com.example.casuskim.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTasks(): Flow<List<Task>>
    fun getTasksByCategory(categoryId: String): Flow<List<Task>>
    suspend fun getTaskById(id: String): Task?
    suspend fun insertTasks(tasks: List<Task>)
    suspend fun deleteAllTasks()
    suspend fun getRandomTasksByCategories(categoryIds: List<String>, count: Int): List<Task>
}
