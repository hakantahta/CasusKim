package com.example.casuskim.domain.model

data class Category(
    val id: String,
    val name: String,
    val description: String,
    val icon: String
)

data class Task(
    val id: String,
    val categoryId: String,
    val title: String,
    val description: String,
    val difficulty: TaskDifficulty,
    val estimatedTime: Int // dakika cinsinden
)

enum class TaskDifficulty {
    EASY, MEDIUM, HARD
}

data class Player(
    val id: String,
    val name: String,
    val color: String
)

data class GameSession(
    val id: String,
    val players: List<Player>,
    val selectedCategories: List<String>,
    val gameDuration: Int, // dakika cinsinden
    val isMixedCategories: Boolean,
    val startTime: Long,
    val endTime: Long? = null
)

data class PlayerTask(
    val playerId: String,
    val taskId: String,
    val assignedAt: Long,
    val completedAt: Long? = null,
    val isCompleted: Boolean = false
)

data class GameResult(
    val sessionId: String,
    val players: List<Player>,
    val playerTasks: List<PlayerTask>,
    val totalTasks: Int,
    val completedTasks: Int,
    val gameDuration: Int,
    val startTime: Long,
    val endTime: Long
)
