package com.example.casuskim.domain.model

data class Category(
    val id: String,
    val name: String,
    val description: String,
    val icon: String
)

data class Word(
    val id: String,
    val categoryId: String,
    val word: String,
    val description: String,
    val relatedQuestions: List<String> // Casus oyunu için sorular
)

data class Player(
    val id: String,
    val name: String,
    val color: String,
    val isSpy: Boolean = false,
    val assignedWord: String? = null
)

data class GameSession(
    val id: String,
    val players: List<Player>,
    val selectedCategory: String,
    val gameDuration: Int, // dakika cinsinden
    val startTime: Long,
    val endTime: Long? = null,
    val currentPlayerIndex: Int = 0,
    val isGameStarted: Boolean = false,
    val isGameFinished: Boolean = false
)

data class GameResult(
    val sessionId: String,
    val players: List<Player>,
    val spyPlayerId: String?,
    val spyWasFound: Boolean,
    val gameDuration: Int,
    val startTime: Long,
    val endTime: Long
)
