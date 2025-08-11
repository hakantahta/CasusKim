package com.example.casuskim.domain.usecase

import com.example.casuskim.domain.model.Task
import com.example.casuskim.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetTasksUseCase(
    private val taskRepository: TaskRepository
) {
    fun getTasksByCategory(categoryId: String): Flow<List<Task>> {
        return taskRepository.getTasksByCategory(categoryId)
    }
    
    suspend fun getRandomTasksByCategories(categoryIds: List<String>, count: Int): List<Task> {
        return taskRepository.getRandomTasksByCategories(categoryIds, count)
    }
}
