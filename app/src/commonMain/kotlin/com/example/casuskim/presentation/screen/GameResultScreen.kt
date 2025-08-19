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
import com.example.casuskim.data.local.GameState
import com.example.casuskim.presentation.screen.HomeScreen
import com.example.casuskim.presentation.screen.CategorySelectionScreen
import com.example.casuskim.presentation.util.toColor

class GameResultScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        
        val players = GameState.players
        
        val spyPlayer = players.find { it.isSpy }
        val spyFinding = remember { GameState.computeSpyFinding() }
        val spyWasFound = spyFinding.spyWasFound
        
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
                    text = "Toplam süre: ${GameState.gameDurationMinutes} dakika",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(32.dp))

                         // Oyun sonucu
             if (spyPlayer != null) {
                 Card(
                     modifier = Modifier.fillMaxWidth(),
                     colors = CardDefaults.cardColors(
                         containerColor = if (spyWasFound) {
                             MaterialTheme.colorScheme.primaryContainer
                         } else {
                             MaterialTheme.colorScheme.errorContainer
                         }
                     )
                 ) {
                     Column(
                         modifier = Modifier.padding(24.dp),
                         horizontalAlignment = Alignment.CenterHorizontally
                     ) {
                         Text(
                             text = if (spyWasFound) "🎯 CASUS YAKALANDI! 🎯" else "🎭 CASUS KAZANDI! 🎭",
                             fontSize = 18.sp,
                             fontWeight = FontWeight.Bold,
                             color = if (spyWasFound) {
                                 MaterialTheme.colorScheme.onPrimaryContainer
                             } else {
                                 MaterialTheme.colorScheme.onErrorContainer
                             }
                         )
                         
                         Spacer(modifier = Modifier.height(16.dp))
                         
                         Box(
                             modifier = Modifier
                                 .size(80.dp)
                                 .background(
                                     color = spyPlayer.color.toColor(),
                                     shape = androidx.compose.foundation.shape.CircleShape
                                 ),
                             contentAlignment = Alignment.Center
                         ) {
                             Text(
                                 text = spyPlayer.name.firstOrNull()?.uppercase() ?: "",
                                 color = androidx.compose.ui.graphics.Color.White,
                                 fontWeight = FontWeight.Bold,
                                 fontSize = 24.sp
                             )
                         }
                         
                         Spacer(modifier = Modifier.height(16.dp))
                         
                         Text(
                             text = spyPlayer.name,
                             fontSize = 24.sp,
                             fontWeight = FontWeight.Bold,
                             color = if (spyWasFound) {
                                 MaterialTheme.colorScheme.onPrimaryContainer
                             } else {
                                 MaterialTheme.colorScheme.onErrorContainer
                             }
                         )
                         
                         Text(
                             text = if (spyWasFound) "Casus yakalandı!" else "Casus gizlendi!",
                             fontSize = 16.sp,
                             color = if (spyWasFound) {
                                 MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                             } else {
                                 MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f)
                             }
                         )
                     }
                 }
             }
             
             Spacer(modifier = Modifier.height(24.dp))
             
             // Oy birikimi özeti
             val voteCounts = remember { GameState.getVoteCounts() }
             if (voteCounts.isNotEmpty()) {
                 Spacer(modifier = Modifier.height(8.dp))
                 Card(
                     modifier = Modifier.fillMaxWidth()
                 ) {
                     Column(modifier = Modifier.padding(16.dp)) {
                         Text("Oy Dökümü", fontWeight = FontWeight.SemiBold)
                         Spacer(modifier = Modifier.height(8.dp))
                         players.forEach { p ->
                             Row(
                                 modifier = Modifier.fillMaxWidth(),
                                 horizontalArrangement = Arrangement.SpaceBetween
                             ) {
                                 Text(p.name)
                                 Text("${voteCounts[p.id] ?: 0} oy")
                             }
                         }
                     }
                 }
             }

             Spacer(modifier = Modifier.height(16.dp))

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
                     PlayerResultCard(
                         player = player,
                         isSpy = player.isSpy,
                         word = player.assignedWord
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
                    onClick = {
                        // Aynı oyuncular kalsın, sadece yeni kelime ve casus ata
                        GameState.clearRoundAssignments()
                        GameState.assignSpyAndWord()
                        navigator.push(CardRevealScreen())
                    },
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

@Composable
private fun PlayerResultCard(
    player: Player,
    isSpy: Boolean,
    word: String?
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
            // Oyuncu avatar
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
            
            // Oyuncu bilgileri
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = player.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                
                if (isSpy) {
                    Text(
                        text = "🎭 Casus",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = "Kelime: ${word ?: "-"}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Casus ikonu
            if (isSpy) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
