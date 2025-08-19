package com.example.casuskim.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.casuskim.presentation.screen.CategorySelectionScreen
import com.example.casuskim.presentation.screen.SettingsScreen

class HomeScreen(
	private val imageResId: Int = 0
) : Screen {
	@Composable
	override fun Content() {
		val navigator = LocalNavigator.currentOrThrow

		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(24.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			Text(
				text = "Casus Kim",
				fontSize = 32.sp,
				fontWeight = FontWeight.Bold,
				color = MaterialTheme.colorScheme.primary
			)

			if (imageResId != 0) {
				Image(
					painter = painterResource(id = imageResId),
					contentDescription = "Oyun İkonu",
					modifier = Modifier
						.size(200.dp)
						.padding(top = 16.dp, bottom = 16.dp)
				)
			}

			Spacer(modifier = Modifier.height(16.dp))

			Text(
				text = "Sırayla kelimene bak, casusu bulmaya çalış!",
				fontSize = 16.sp,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				textAlign = androidx.compose.ui.text.style.TextAlign.Center
			)

			Spacer(modifier = Modifier.height(48.dp))

			Button(
				onClick = { navigator.push(CategorySelectionScreen()) },
				modifier = Modifier
					.fillMaxWidth()
					.height(56.dp)
			) {
				Text(
					text = "Yeni Oyun Başlat",
					fontSize = 18.sp,
					fontWeight = FontWeight.Medium
				)
			}

			Spacer(modifier = Modifier.height(16.dp))

			OutlinedButton(
				onClick = { navigator.push(SettingsScreen()) },
				modifier = Modifier
					.fillMaxWidth()
					.height(56.dp)
			) {
				Text(
					text = "Ayarlar",
					fontSize = 18.sp,
					fontWeight = FontWeight.Medium
				)
			}
		}
	}
}
