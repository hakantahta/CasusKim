package com.example.casuskim.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.casuskim.domain.model.Player
import com.example.casuskim.domain.model.Task
import com.example.casuskim.domain.model.TaskDifficulty
import com.example.casuskim.presentation.screen.GameResultScreen
import com.example.casuskim.presentation.util.toColor
import kotlinx.coroutines.delay

class GameScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var isGameStarted by remember { mutableStateOf(false) }
        var isPaused by remember { mutableStateOf(false) }
        var remainingTime by remember { mutableStateOf(30 * 60) } // 30 dakika
        var currentPlayerIndex by remember { mutableStateOf(0) }
        
        // Örnek oyuncular ve görevler
        val players = remember {
            listOf(
                Player("1", "Ahmet", "#FF6B6B"),
                Player("2", "Ayşe", "#4ECDC4"),
                Player("3", "Mehmet", "#45B7D1")
            )
        }
        
        val tasks = remember {
            listOf(
                Task("1", "1", "10 şınav çek", "Fiziksel güç gerektiren görev", TaskDifficulty.EASY, 5),
                Task("2", "2", "Bir resim çiz", "Yaratıcılık gerektiren görev", TaskDifficulty.MEDIUM, 10),
                Task("3", "3", "Basit bir deney yap", "Bilimsel merak gerektiren görev", TaskDifficulty.HARD, 15)
            )
        }
        
        LaunchedEffect(isGameStarted, isPaused) {
            while (isGameStarted && !isPaused && remainingTime > 0) {
                delay(1000)
                remainingTime--
                
                if (remainingTime <= 0) {
                    isGameStarted = false
                    navigator.push(GameResultScreen())
                }
            }
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Üst bar - Zaman ve kontroller
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Kalan Süre: ${remainingTime / 60}:${(remainingTime % 60).toString().padStart(2, '0')}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Row {
                                        if (!isGameStarted) {
                        Button(
                            onClick = { isGameStarted = true }
                        ) {
                            Text("Başlat")
                        }
                    } else {
                        Button(
                            onClick = { isPaused = !isPaused }
                        ) {
                            Text(if (isPaused) "Devam Et" else "Duraklat")
                        }
                        
                        Button(
                            onClick = { 
                                isGameStarted = false
                                navigator.push(GameResultScreen())
                            }
                        ) {
                            Text("Bitir")
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Mevcut oyuncu
            if (isGameStarted && players.isNotEmpty()) {
                val currentPlayer = players[currentPlayerIndex]
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Sıra: ${currentPlayer.name}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        if (tasks.isNotEmpty()) {
                            val currentTask = tasks[currentPlayerIndex % tasks.size]
                            Text(
                                text = currentTask.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = currentTask.description,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Button(
                                onClick = {
                                    currentPlayerIndex = (currentPlayerIndex + 1) % players.size
                                }
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Görevi Tamamla")
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Oyuncu listesi
            Text(
                text = "Oyuncular",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(players) { player ->
                    GamePlayerCard(
                        player = player,
                        isCurrentPlayer = players.indexOf(player) == currentPlayerIndex && isGameStarted,
                        completedTasks = 0 // Gerçek uygulamada repository'den gelecek
                    )
                }
            }
        }
    }
}

@Composable
private fun GamePlayerCard(
    player: Player,
    isCurrentPlayer: Boolean,
    completedTasks: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentPlayer) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
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
                    .size(40.dp)
                                            .background(
                            color = player.color.toColor(),
                            shape = androidx.compose.foundation.shape.CircleShape
                        ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = player.name.firstOrNull()?.uppercase() ?: "",
                    color = androidx.compose.ui.graphics.Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = player.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = "Tamamlanan görevler: $completedTasks",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            if (isCurrentPlayer) {
                Text(
                    text = "SIRA",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
