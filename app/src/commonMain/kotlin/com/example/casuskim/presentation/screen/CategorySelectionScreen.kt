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

class CategorySelectionScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var selectedCategories by remember { mutableStateOf(setOf<String>()) }
        var isMixedCategories by remember { mutableStateOf(false) }
        
        // Örnek kategoriler (gerçek uygulamada repository'den gelecek)
        val categories = remember {
            listOf(
                Category("1", "Spor", "Fiziksel aktiviteler", "🏃‍♂️"),
                Category("2", "Sanat", "Yaratıcı görevler", "🎨"),
                Category("3", "Bilim", "Bilimsel deneyler", "🔬"),
                Category("4", "Müzik", "Müzikle ilgili görevler", "🎵"),
                Category("5", "Yemek", "Mutfak görevleri", "🍳"),
                Category("6", "Doğa", "Doğa aktiviteleri", "🌿")
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
                text = "Oyun için kategorileri seçin veya tüm kategorileri karıştırın",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Karışık kategoriler seçeneği
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isMixedCategories,
                    onCheckedChange = { 
                        isMixedCategories = it
                        if (it) selectedCategories = emptySet()
                    }
                )
                Text(
                    text = "Tüm kategorileri karıştır",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (!isMixedCategories) {
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
                                    selectedCategories + category.id
                                } else {
                                    selectedCategories - category.id
                                }
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
                            Button(
                    onClick = { 
                        if (isMixedCategories || selectedCategories.isNotEmpty()) {
                            navigator.push(PlayerSetupScreen())
                        }
                    },
                    enabled = isMixedCategories || selectedCategories.isNotEmpty(),
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
