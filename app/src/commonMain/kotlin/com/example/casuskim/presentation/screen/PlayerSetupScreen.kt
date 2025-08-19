package com.example.casuskim.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import com.example.casuskim.presentation.screen.GameScreen
import com.example.casuskim.presentation.screen.CardRevealScreen
import com.example.casuskim.data.local.GameState
import com.example.casuskim.presentation.util.toColor
import kotlinx.datetime.Clock

class PlayerSetupScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var players by remember { mutableStateOf(GameState.players) }
        var newPlayerName by remember { mutableStateOf("") }
        var gameDuration by remember { mutableStateOf(GameState.gameDurationMinutes) }
        
        val playerColors = remember {
            listOf(
                "#FF6B6B", "#4ECDC4", "#45B7D1", "#96CEB4",
                "#FFEAA7", "#DDA0DD", "#98D8C8", "#F7DC6F"
            )
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Oyuncu Ekleme",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Oyuncuları ekleyin ve oyun süresini ayarlayın",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Oyun süresi ayarı
            Text(
                text = "Oyun Süresi (dakika)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Slider(
                    value = gameDuration.toFloat(),
                    onValueChange = { gameDuration = it.toInt() },
                    valueRange = 10f..120f,
                    steps = 21,
                    modifier = Modifier.weight(1f)
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Text(
                    text = "$gameDuration dk",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Oyuncu ekleme
            Text(
                text = "Oyuncular",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newPlayerName,
                    onValueChange = { newPlayerName = it },
                    label = { Text("Oyuncu adı") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                FloatingActionButton(
                    onClick = {
                        if (newPlayerName.isNotBlank() && players.size < 8) {
                            val newPlayer = Player(
                                id = Clock.System.now().toEpochMilliseconds().toString(),
                                name = newPlayerName,
                                color = playerColors[players.size % playerColors.size]
                            )
                            players = players + newPlayer
                            newPlayerName = ""
                        }
                    },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Oyuncu Ekle")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Oyuncu listesi
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(players) { index, player ->
                    PlayerCard(
                        player = player,
                        onDelete = {
                            players = players.filter { it.id != player.id }
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                    onClick = { 
                        if (players.isNotEmpty()) {
                            GameState.setPlayers(players)
                            GameState.setGameDuration(gameDuration)
                            GameState.clearRoundAssignments()
                            GameState.assignSpyAndWord()
                            navigator.push(CardRevealScreen())
                        }
                    },
                    enabled = players.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                Text(
                    text = "Oyunu Başlat",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun PlayerCard(
    player: Player,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
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
            
            Text(
                text = player.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Oyuncuyu Sil",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
