package com.example.casuskim.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
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

class SettingsScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var showDeleteConfirmation by remember { mutableStateOf(false) }
        var showAboutDialog by remember { mutableStateOf(false) }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Üst bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navigator.pop() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Text(
                    text = "Ayarlar",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Ayarlar listesi
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    // Uygulama hakkında
                    ListItem(
                        headlineContent = {
                            Text(
                                text = "Uygulama Hakkında",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        leadingContent = {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        modifier = Modifier.clickable { showAboutDialog = true }
                    )
                    
                    Divider()
                    
                    // Verileri temizle
                    ListItem(
                        headlineContent = {
                            Text(
                                text = "Tüm Verileri Temizle",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.error
                            )
                        },
                        supportingContent = {
                            Text(
                                text = "Oyun geçmişi ve ayarlar silinecek",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        leadingContent = {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        },
                        modifier = Modifier.clickable { showDeleteConfirmation = true }
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Versiyon bilgisi
            Text(
                text = "Casus Oyunu v1.0.0",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        
        // Silme onay dialog'u
        if (showDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmation = false },
                title = {
                    Text("Verileri Temizle")
                },
                text = {
                    Text("Tüm oyun geçmişi ve ayarlar kalıcı olarak silinecek. Bu işlem geri alınamaz.")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // TODO: Verileri temizle
                            showDeleteConfirmation = false
                        }
                    ) {
                        Text("Temizle", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirmation = false }) {
                        Text("İptal")
                    }
                }
            )
        }
        
        // Hakkında dialog'u
        if (showAboutDialog) {
            AlertDialog(
                onDismissRequest = { showAboutDialog = false },
                title = {
                    Text("Casus Oyunu Hakkında")
                },
                text = {
                    Column {
                        Text("Casus Oyunu, arkadaşlarınızla birlikte oynayabileceğiniz eğlenceli bir görev tabanlı oyundur.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Özellikler:")
                        Text("• Çoklu kategori desteği")
                        Text("• Özelleştirilebilir oyun süresi")
                        Text("• Offline çalışma")
                        Text("• Modern ve kullanıcı dostu arayüz")
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showAboutDialog = false }) {
                        Text("Tamam")
                    }
                }
            )
        }
    }
}
