package com.example.casuskim.presentation.navigation

sealed class AppScreen {
    object Home : AppScreen()
    object CategorySelection : AppScreen()
    object PlayerSetup : AppScreen()
    object Game : AppScreen()
    object GameResult : AppScreen()
    object Settings : AppScreen()
}
