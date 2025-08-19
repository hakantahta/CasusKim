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
import com.example.casuskim.presentation.screen.GameResultScreen
import com.example.casuskim.data.local.GameState
import com.example.casuskim.presentation.util.toColor
import kotlinx.coroutines.delay

class GameScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var isGameStarted by remember { mutableStateOf(false) }
        var remainingTime by remember { mutableStateOf(GameState.gameDurationMinutes * 60) }
        val players = GameState.players
        
        LaunchedEffect(isGameStarted) {
            while (isGameStarted && remainingTime > 0) {
                delay(1000)
                remainingTime--
                
                if (remainingTime <= 0) {
                    isGameStarted = false
                    navigator.push(VotingScreen())
                }
            }
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Orta: sadece sayaç
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${remainingTime / 60}:${(remainingTime % 60).toString().padStart(2, '0')}",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Alt: Kontroller
            if (!isGameStarted) {
                Button(
                    onClick = { isGameStarted = true },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Süreyi Başlat") }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { 
                            isGameStarted = false
                            navigator.push(VotingScreen())
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Süreyi Bitir ve Oylamaya Geç") }

                    OutlinedButton(
                        onClick = { 
                            isGameStarted = false
                            navigator.push(VotingScreen())
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Süre Bitmeden Oylamaya Geç") }
                }
            }
        }
    }
}
