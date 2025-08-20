package com.example.casuskim.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person

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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.surface
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Oyuncu bilgisi kartı
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        AnimatedContent(
                            targetState = currentPlayer.name,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                            }
                        ) { targetName ->
                            Text(
                                text = "Sıra: $targetName",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = if (!isRevealed) "Kartı görmek için sola kaydırın" else "Kartı kapatmak için sağa kaydırın",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Ana kart
                val targetCardColor = if (isRevealed) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                val animatedCardColor by animateColorAsState(
                    targetValue = targetCardColor,
                    animationSpec = tween(durationMillis = 300)
                )
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .shadow(
                            elevation = 16.dp,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .pointerInput(isRevealed) {
                            detectHorizontalDragGestures { change, dragAmount ->
                                if (!isRevealed && dragAmount < -20) {
                                    isRevealed = true
                                } else if (isRevealed && dragAmount > 20) {
                                    isRevealed = false
                                }
                            }
                        },
                    shape = RoundedCornerShape(24.dp),
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
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = if (currentPlayer.isSpy) "🎭" else "📝",
                                        fontSize = 48.sp
                                    )
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    Text(
                                        text = if (currentPlayer.isSpy) "CASUS" else (currentPlayer.assignedWord ?: ""),
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        textAlign = TextAlign.Center
                                    )
                                    
                                    if (currentPlayer.isSpy) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Diğer oyuncuları kandırmaya çalış!",
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            } else {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "🃏",
                                        fontSize = 48.sp
                                    )
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    Text(
                                        text = "Kart Kapalı",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center
                                    )
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                    
                                    Text(
                                        text = "Sola kaydırarak kartını görebilirsin",
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Alt buton
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
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
                        modifier = Modifier.fillMaxSize(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = if (!isRevealed) "Sıradaki Oyuncuya Geç" else "Kartı Kapat",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}


