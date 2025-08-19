package com.example.casuskim.data.local

import com.example.casuskim.domain.model.Player
import kotlin.random.Random

object GameState {
    var players: List<Player> = emptyList()
        private set

    var selectedCategoryId: String? = null
        private set

    var gameDurationMinutes: Int = 30
        private set

    var wordForRound: String? = null
        private set

    // Oylar: voterId -> targetPlayerId
    var votes: MutableMap<String, String> = mutableMapOf()
        private set

    fun setPlayers(newPlayers: List<Player>) {
        players = newPlayers
    }

    fun setCategory(categoryId: String) {
        selectedCategoryId = categoryId
    }

    fun setGameDuration(minutes: Int) {
        gameDurationMinutes = minutes
    }

    fun assignSpyAndWord() {
        if (players.isEmpty() || selectedCategoryId == null) return

        val spyIndex = Random.nextInt(players.size)
        val chosenWord = pickRandomWord(selectedCategoryId!!)
        wordForRound = chosenWord

        players = players.mapIndexed { index, player ->
            player.copy(
                isSpy = index == spyIndex,
                assignedWord = if (index == spyIndex) "CASUS" else chosenWord
            )
        }
    }

    fun clearRoundAssignments() {
        wordForRound = null
        players = players.map { it.copy(isSpy = false, assignedWord = null) }
        clearVotes()
    }

    private fun pickRandomWord(categoryId: String): String {
        val words = when (categoryId) {
            "1" -> listOf("Futbol", "Basketbol", "Voleybol", "Tenis") // Spor
            "2" -> listOf("Pizza", "Hamburger", "Makarna", "Kebap") // Yemek
            "3" -> listOf("Doktor", "Öğretmen", "Mühendis", "Avukat") // Meslek
            "4" -> listOf("Kedi", "Köpek", "Aslan", "Fil") // Hayvan
            "5" -> listOf("Türkiye", "İtalya", "Japonya", "Almanya") // Ülke
            "6" -> listOf("Kırmızı", "Mavi", "Yeşil", "Sarı") // Renk
            else -> listOf("Elma", "Deniz", "Güneş", "Kitap")
        }
        return words.random()
    }

    fun clearVotes() {
        votes.clear()
    }

    fun recordVote(voterId: String, targetPlayerId: String) {
        votes[voterId] = targetPlayerId
    }

    fun getVoteCounts(): Map<String, Int> {
        val counts = mutableMapOf<String, Int>()
        players.forEach { counts[it.id] = 0 }
        votes.values.forEach { targetId ->
            counts[targetId] = (counts[targetId] ?: 0) + 1
        }
        return counts
    }

    fun computeSpyFinding(): SpyFindingResult {
        val counts = getVoteCounts()
        if (counts.isEmpty()) return SpyFindingResult(spyWasFound = false, votedPlayerId = null, isTie = false)
        val maxCount = counts.values.maxOrNull() ?: 0
        val topCandidates = counts.filterValues { it == maxCount }.keys
        if (topCandidates.isEmpty()) return SpyFindingResult(spyWasFound = false, votedPlayerId = null, isTie = false)
        if (topCandidates.size > 1) {
            return SpyFindingResult(spyWasFound = false, votedPlayerId = null, isTie = true)
        }
        val topId = topCandidates.first()
        val votedPlayer = players.find { it.id == topId }
        val spyFound = votedPlayer?.isSpy == true
        return SpyFindingResult(spyWasFound = spyFound, votedPlayerId = topId, isTie = false)
    }
}

data class SpyFindingResult(
    val spyWasFound: Boolean,
    val votedPlayerId: String?,
    val isTie: Boolean
)


