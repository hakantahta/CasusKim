package com.example.casuskim.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
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
import com.example.casuskim.presentation.screen.HomeScreen
import com.example.casuskim.presentation.screen.CategorySelectionScreen
import com.example.casuskim.presentation.util.toColor

class GameResultScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        
        // Örnek oyun sonuçları
        val players = remember {
            listOf(
                Player("1", "Ahmet", "#FF6B6B"),
                Player("2", "Ayşe", "#4ECDC4"),
                Player("3", "Mehmet", "#45B7D1")
            )
        }
        
        val playerResults = remember {
            listOf(
                PlayerResult("1", "Ahmet", 5, "#FF6B6B"),
                PlayerResult("2", "Ayşe", 3, "#4ECDC4"),
                PlayerResult("3", "Mehmet", 4, "#45B7D1")
            ).sortedByDescending { it.completedTasks }
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Başlık
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Oyun Bitti!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Toplam süre: 25 dakika",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Kazanan
            if (playerResults.isNotEmpty()) {
                val winner = playerResults.first()
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
                            text = "🏆 KAZANAN 🏆",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                                        .background(
                            color = winner.color.toColor(),
                            shape = androidx.compose.foundation.shape.CircleShape
                        ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = winner.name.firstOrNull()?.uppercase() ?: "",
                                color = androidx.compose.ui.graphics.Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = winner.name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        
                        Text(
                            text = "${winner.completedTasks} görev tamamladı",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Sıralama
            Text(
                text = "Sıralama",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(playerResults) { playerResult ->
                    PlayerResultCard(
                        playerResult = playerResult,
                        rank = playerResults.indexOf(playerResult) + 1
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Butonlar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { navigator.push(HomeScreen()) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Home, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ana Menü")
                }
                
                Button(
                    onClick = { navigator.push(CategorySelectionScreen()) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Yeni Oyun")
                }
            }
        }
    }
}

data class PlayerResult(
    val id: String,
    val name: String,
    val completedTasks: Int,
    val color: String
)

@Composable
private fun PlayerResultCard(
    playerResult: PlayerResult,
    rank: Int
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
            // Sıralama
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = when (rank) {
                            1 -> MaterialTheme.colorScheme.primary
                            2 -> MaterialTheme.colorScheme.secondary
                            3 -> MaterialTheme.colorScheme.tertiary
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rank.toString(),
                    color = if (rank <= 3) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Oyuncu avatar
            Box(
                modifier = Modifier
                    .size(40.dp)
                                            .background(
                            color = playerResult.color.toColor(),
                            shape = androidx.compose.foundation.shape.CircleShape
                        ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = playerResult.name.firstOrNull()?.uppercase() ?: "",
                    color = androidx.compose.ui.graphics.Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Oyuncu bilgileri
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = playerResult.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = "${playerResult.completedTasks} görev tamamladı",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Ödül ikonu
            if (rank == 1) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
