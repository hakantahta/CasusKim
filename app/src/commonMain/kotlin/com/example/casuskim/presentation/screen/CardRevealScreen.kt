package com.example.casuskim.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith

class CardRevealScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var currentIndex by remember { mutableStateOf(0) }
        var isRevealed by remember { mutableStateOf(false) }

        val players = GameState.players

        if (players.isEmpty()) {
            // Emniyet: oyuncu yoksa ana ekrana dön
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
            AnimatedContent(
                targetState = currentPlayer.name,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                }
            ) {
                Text(
                    text = "Sıra: $it",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = if (!isRevealed) "Kartı görmek için sola kaydırın" else "Kartı kapatmak için sağa kaydırın",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            val targetCardColor = if (isRevealed) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
            val animatedCardColor by animateColorAsState(
                targetValue = targetCardColor,
                animationSpec = tween(durationMillis = 300)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .pointerInput(isRevealed) {
                        detectHorizontalDragGestures { change, dragAmount ->
                            if (!isRevealed && dragAmount < -20) {
                                isRevealed = true
                            } else if (isRevealed && dragAmount > 20) {
                                isRevealed = false
                            }
                        }
                    },
                colors = CardDefaults.cardColors(
                    containerColor = animatedCardColor
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Crossfade(targetState = isRevealed, animationSpec = tween(durationMillis = 300)) { revealed ->
                        if (revealed) {
                            Text(
                                text = if (currentPlayer.isSpy) "CASUS" else (currentPlayer.assignedWord ?: ""),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        } else {
                            Text(
                                text = "Kart Kapalı",
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (isRevealed) {
                        isRevealed = false
                    } else {
                        // kart kapalıyken sıradaki oyuncuya geç
                        val next = currentIndex + 1
                        if (next < players.size) {
                            currentIndex = next
                        } else {
                            navigator.push(GameScreen())
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (!isRevealed) "Sıradaki Oyuncuya Geç" else "Kartı Kapat")
            }
        }
    }
}


