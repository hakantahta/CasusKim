package com.example.casuskim.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.clickable
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
import com.example.casuskim.domain.model.Category
import com.example.casuskim.presentation.screen.PlayerSetupScreen
import com.example.casuskim.data.local.GameState

class CategorySelectionScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var selectedCategories by remember { mutableStateOf(setOf<String>()) }
        
        // Örnek kategoriler (gerçek uygulamada repository'den gelecek)
        val categories = remember {
            listOf(
                Category("1", "Spor", "Futbol, basketbol, yüzme gibi sporlar", "⚽"),
                Category("2", "Yemek", "Pizza, hamburger, makarna gibi yemekler", "🍕"),
                Category("3", "Meslek", "Doktor, öğretmen, mühendis gibi meslekler", "👨‍⚕️"),
                Category("4", "Hayvan", "Kedi, köpek, aslan gibi hayvanlar", "🐱"),
                Category("5", "Ülke", "Türkiye, İtalya, Japonya gibi ülkeler", "🇹🇷"),
                Category("6", "Renk", "Kırmızı, mavi, yeşil gibi renkler", "🎨")
            )
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Kategori Seçimi",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Oyun için bir kategori seçin",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(categories) { category ->
                    CategoryCard(
                        category = category,
                        isSelected = selectedCategories.contains(category.id),
                        onSelectionChanged = { isSelected ->
                            selectedCategories = if (isSelected) {
                                setOf(category.id) // Sadece bir kategori seçilebilir
                            } else {
                                emptySet()
                            }
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { 
                    if (selectedCategories.isNotEmpty()) {
                        val chosen = selectedCategories.first()
                        GameState.setCategory(chosen)
                        navigator.push(PlayerSetupScreen())
                    }
                },
                enabled = selectedCategories.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Devam Et",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryCard(
    category: Category,
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        onClick = { onSelectionChanged(!isSelected) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = category.icon,
                fontSize = 32.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = category.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
