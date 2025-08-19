package com.example.casuskim.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.casuskim.data.local.GameState
import com.example.casuskim.presentation.util.toColor

class VotingScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val players = GameState.players

        var currentIndex by remember { mutableStateOf(0) }
        var selectedTargetId by remember { mutableStateOf<String?>(null) }

        if (players.isEmpty()) {
            navigator.pop()
            return
        }

        val currentPlayer = players[currentIndex]

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Oylama - Sıra: ${currentPlayer.name}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Bu oyuncu gizli oyunu veriyor. Telefonu yalnızca bu oyuncu tutsun.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Oy hedefi seçimi
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                players.filter { it.id != currentPlayer.id }.forEach { candidate ->
                    val isSelected = selectedTargetId == candidate.id
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedTargetId = candidate.id },
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        color = candidate.color.toColor(),
                                        shape = androidx.compose.foundation.shape.CircleShape
                                    )
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = candidate.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val target = selectedTargetId
                    if (target != null) {
                        GameState.recordVote(currentPlayer.id, target)
                        selectedTargetId = null
                        val next = currentIndex + 1
                        if (next < players.size) {
                            currentIndex = next
                        } else {
                            navigator.push(VotingSummaryScreen())
                        }
                    }
                },
                enabled = selectedTargetId != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Oyunu Onayla ve Sıradaki Oyuncuya Ver")
            }
        }
    }
}

class VotingSummaryScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val voteCounts = GameState.getVoteCounts()
        val players = GameState.players
        val result = GameState.computeSpyFinding()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Oylama Sonuçları",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sonuç özeti
            val summaryText = when {
                result.isTie -> "Eşitlik var! Casus yakalanamadı."
                result.votedPlayerId == null -> "Oy kullanılmadı."
                result.spyWasFound -> "Çoğunluk casusu buldu!"
                else -> "Casus bulunamadı."
            }
            Text(summaryText)

            Spacer(modifier = Modifier.height(16.dp))

            // Oy dökümü
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                players.forEach { p ->
                    val count = voteCounts[p.id] ?: 0
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(p.name)
                        Text("${count} oy")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navigator.push(GameResultScreen())
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Sonuç Ekranına Geç") }
        }
    }
}


