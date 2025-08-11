package com.example.casuskim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.example.casuskim.presentation.screen.HomeScreen
import com.example.casuskim.ui.theme.CasusKimTheme
import com.example.casuskim.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Koin başlatma
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MainActivity)
            modules(appModule)
        }
        
        enableEdgeToEdge()
        setContent {
            CasusKimTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CasusKimApp()
                }
            }
        }
    }
}

@Composable
fun CasusKimApp() {
    Navigator(HomeScreen()) { navigator ->
        SlideTransition(navigator)
    }
}